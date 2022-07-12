package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.api.RemoteScanApi;
import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.information.request.RequestInformation;
import com.kongrentian.plugins.nexus.model.scanresult.ScanResult;
import org.sonatype.nexus.common.collect.NestedAttributesMap;
import org.sonatype.nexus.repository.storage.AssetStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;


@Named
public class RemoteScanner extends AbstractScanner {

    @Inject
    public RemoteScanner(AssetStore assetStore,
                         BundleHelper bundleHelper) {
        super(assetStore, bundleHelper);
    }


    @Nonnull
    ScanResult scanImpl(RequestInformation information) throws IOException {
        NestedAttributesMap securityAttributes = information
                .getComponent().getAsset().attributes().child("Security");
        ScanResult lastScan = getLastScan(securityAttributes);
        if (lastScan != null) {
            return lastScan;
        }
        RemoteScanApi securityClient = bundleHelper.getSecurityClientApi();
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
                || interval < bundleHelper
                .getBundleConfiguration()
                .getScanners().getRemote()
                .getInterval()) {
            return lastScan;
        }
        return null;
    }

    @Override
    boolean failOnErrors() {
        return bundleHelper
                .getBundleConfiguration()
                .getScanners()
                .getRemote()
                .isFailOnErrors();
    }
}
