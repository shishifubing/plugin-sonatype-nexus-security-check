package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.main.BundleHelper;
import com.kongrentian.plugins.nexus.model.information.request.RequestInformation;
import com.kongrentian.plugins.nexus.model.scanresult.ScanResult;
import com.kongrentian.plugins.nexus.model.scanresult.ScanResultType;
import org.sonatype.nexus.repository.storage.AssetStore;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;

abstract public class AbstractScanner {

    final AssetStore assetStore;
    final BundleHelper bundleHelper;

    public AbstractScanner(final AssetStore assetStore,
                           final BundleHelper bundleHelper) {
        this.assetStore = assetStore;
        this.bundleHelper = bundleHelper;
    }

    public ScanResult scan(RequestInformation information) {
        try {
            return scanImpl(information);
        } catch (Throwable exception) {
            boolean fail = failOnErrors();
            LOG.error("Could not scan asset {}",
                    information.getRequest().getPath(),
                    exception);
            return new ScanResult(
                    !fail,
                    fail ? ScanResultType.EXCEPTION
                            : ScanResultType.EXCEPTION_IGNORED)
                    .setException(exception);
        }
    }

    abstract ScanResult scanImpl(RequestInformation information) throws Throwable;

    abstract boolean failOnErrors();
}
