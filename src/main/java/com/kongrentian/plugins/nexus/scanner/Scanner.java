package com.kongrentian.plugins.nexus.scanner;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongrentian.plugins.nexus.api.ClientAPI;
import com.kongrentian.plugins.nexus.model.CheckRequest;
import com.kongrentian.plugins.nexus.model.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.collect.AttributesMap;
import org.sonatype.nexus.common.collect.NestedAttributesMap;
import org.sonatype.nexus.repository.search.DefaultComponentMetadataProducer;
import org.sonatype.nexus.repository.storage.*;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Payload;
import org.sonatype.nexus.repository.Repository;
import retrofit2.Response;


@Named
public class Scanner {
    private static final Logger LOG = LoggerFactory.getLogger(Scanner.class);

    private final AssetStore assetStore;

    private final ComponentStore componentStore;

    @Inject
    public Scanner(final AssetStore assetStore, final ComponentStore componentStore) {
       this.assetStore = assetStore;
       this.componentStore = componentStore;
    }

    ScanResult scan(@Nonnull org.sonatype.nexus.repository.view.Response response,
                    @Nonnull Repository repository,
                    ClientAPI clientAPI) throws Exception {
        Payload payload = response.getPayload();
        if (!(payload instanceof Content)) {
            return null;
        }
        AttributesMap attributes = ((Content) payload).getAttributes();
        Asset asset = attributes.get(Asset.class);
        if (asset == null) {
            return null;
        }
        NestedAttributesMap securityAttributes = asset.attributes().child("Security");
        if (skipScan(securityAttributes)) {
            return null;
        }
        Component component = componentStore.read(asset.componentId());

        CheckRequest request = new CheckRequest(repository, response, asset, component);
        LOG.info("request - {}", new ObjectMapper().writeValueAsString(request));
        Response<ScanResult> responseCheck = clientAPI.check(request).execute();
        String message = responseCheck.message();
        LOG.info("Security check response: {}", message);
        ScanResult scanResult = responseCheck.body();
        if (!responseCheck.isSuccessful() || scanResult == null) {
            throw new RuntimeException("Could not check " + asset
                                       + ", code - " + responseCheck.code()
                                       + ", response: " +  message);
        }
        updateAssetAttributes(scanResult, securityAttributes);
        assetStore.save(asset);

        return scanResult;
    }

    private boolean skipScan(NestedAttributesMap securityAttributes) {
        if (securityAttributes == null || securityAttributes.isEmpty()) {
            return false;
        }
        Instant checkDate =  (Instant) securityAttributes.get("security_check_date");
        if (checkDate == null) {
            return false;
        }
        return ChronoUnit.HOURS.between(checkDate, Instant.now()) <= 24;
    }

    private void updateAssetAttributes(@Nonnull ScanResult scanResult,
                                       @Nonnull NestedAttributesMap securityAttributes) {
        securityAttributes.clear();
        securityAttributes.set("security_issue", scanResult.issue);
        securityAttributes.set("security_allowed", scanResult.allowed);
        securityAttributes.set("security_check_date", scanResult.checkDate);
        securityAttributes.set("security_reason", scanResult.reason);
    }

}
