package com.shishifubing.plugins.nexus.scanner;

import com.shishifubing.plugins.nexus.main.BundleHelper;
import com.shishifubing.plugins.nexus.model.bundle.configuration.BundleConfiguration;
import com.shishifubing.plugins.nexus.model.bundle.configuration.BundleConfigurationScannerLocal;
import com.shishifubing.plugins.nexus.model.information.request.RequestInformation;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResult;
import com.shishifubing.plugins.nexus.model.scanresult.ScanResultType;
import com.shishifubing.plugins.nexus.model.white_list.WhiteList;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.storage.AssetStore;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LocalScanner extends AbstractScanner {

    @Inject
    public LocalScanner(AssetStore assetStore,
                        BundleHelper bundleHelper) {
        super(assetStore, bundleHelper);
    }

    @Override
    ScanResult scanImpl(RequestInformation information) {
        BundleConfiguration config = bundleHelper
                .getBundleConfiguration();
        BundleConfigurationScannerLocal scannerConfig = config
                .getScanners()
                .getLocal();
        ScanResultType scanResultType = scannerConfig
                .getWhiteList()
                .contains(information);
        if (!WhiteList.isFailure(scanResultType)) {
            return new ScanResult(true, scanResultType);
        }
        DateTime lastModified = information.getComponent().getLastModified();
        assert lastModified != null;
        if (lastModified.isBefore(scannerConfig.getLastModified())) {
            return new ScanResult(true, ScanResultType.LAST_MODIFIED_VALID);
        }
        return new ScanResult(false, ScanResultType.LAST_MODIFIED_INVALID);
    }

    @Override
    public boolean failOnErrors() {
        return bundleHelper
                .getBundleConfiguration()
                .getScanners()
                .getLocal()
                .isFailOnErrors();
    }
}
