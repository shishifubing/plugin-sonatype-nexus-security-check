package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.api.RemoteScanApi;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.request_information.RequestInformation;
import com.kongrentian.plugins.nexus.model.scan_result.ScanResult;
import org.sonatype.nexus.common.collect.NestedAttributesMap;
import org.sonatype.nexus.repository.storage.AssetStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;


@Named
public class RemoteScanner extends AbstractScanner {

    @Inject
    public RemoteScanner(AssetStore assetStore,
                         SecurityCapabilityHelper securityCapabilityHelper) {
        super(assetStore, securityCapabilityHelper);
    }


    @Nonnull
    ScanResult scanImpl(RequestInformation information) throws IOException {
        NestedAttributesMap securityAttributes = information
                .getComponent().getAsset().attributes().child("Security");
        ScanResult lastScan = getLastScan(securityAttributes);
        if (lastScan != null) {
            return lastScan;
        }
        RemoteScanApi securityClient = securityCapabilityHelper.getSecurityClientApi();
        Response<ScanResult> responseCheck = securityClient
                .check(information)
                .execute();
        String message = responseCheck.message();
        LOG.debug("Security check response: {}", message);
        ScanResult scanResult = responseCheck.body();
        if (!responseCheck.isSuccessful() || scanResult == null) {
            throw new RuntimeException("Invalid response code "
                    + responseCheck.code() + ": " + message);
        }
        scanResult.updateAssetAttributes(securityAttributes);
        assetStore.save(information.getComponent().getAsset());

        return scanResult;
    }

    @Override
    public SecurityCapabilityKey getFailKey() {
        return SecurityCapabilityKey.SCAN_REMOTE_FAIL_ON_ERRORS;
    }

    @Nullable
    private ScanResult getLastScan(@Nullable NestedAttributesMap securityAttributes) {
        if (securityAttributes == null) {
            return null;
        }
        ScanResult lastScan = ScanResult.fromAttributes(securityAttributes);
        if (lastScan == null) {
            return null;
        }
        long interval = lastScan.getInterval();
        if (interval == ScanResult.NO_LAST_SCAN
                || interval < securityCapabilityHelper
                .getCapabilityConfiguration()
                .getScanRemoteInterval()) {
            return lastScan;
        }
        return null;
    }
}
