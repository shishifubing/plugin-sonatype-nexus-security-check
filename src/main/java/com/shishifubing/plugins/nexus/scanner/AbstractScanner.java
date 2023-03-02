package com.shishifubing.plugins.nexus.scanner;

import com.shishifubing.plugins.nexus.main.BundleHelper;
import com.shishifubing.plugins.nexus.model.information.request.RequestInformation;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResult;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResultType;
import com.shishifubing.plugins.nexus.logging.SecurityLogConfiguration;
import org.sonatype.nexus.repository.storage.AssetStore;

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
            SecurityLogConfiguration.LOG.error("Could not scan asset {}",
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
