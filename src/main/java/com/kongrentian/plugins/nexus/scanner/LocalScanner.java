package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.RequestInformation;
import com.kongrentian.plugins.nexus.model.ScanResult;
import com.kongrentian.plugins.nexus.model.ScanResultType;
import com.kongrentian.plugins.nexus.model.WhiteList;
import org.joda.time.DateTime;
import org.sonatype.nexus.repository.storage.AssetStore;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LocalScanner extends AbstractScanner {

    @Inject
    public LocalScanner(AssetStore assetStore,
                        SecurityCapabilityHelper securityCapabilityHelper) {
        super(assetStore, securityCapabilityHelper);
    }

    @Override
    ScanResult scanImpl(RequestInformation information) {
        SecurityCapabilityConfiguration config = securityCapabilityHelper
                .getCapabilityConfiguration();
        ScanResultType scanResultType = config
                .getScanLocalWhiteList()
                .contains(information);
        if (!WhiteList.isFailure(scanResultType)) {
            return new ScanResult(true, scanResultType);
        }
        DateTime lastModified = information.getComponent().getLastModified();
        assert lastModified != null;
        if (lastModified.isBefore(config.getScanLocalLastModified())) {
            return new ScanResult(true, ScanResultType.LAST_MODIFIED_VALID);
        }
        return new ScanResult(false, ScanResultType.LAST_MODIFIED_INVALID);
    }

    @Override
    public SecurityCapabilityKey getFailKey() {
        return SecurityCapabilityKey.SCAN_LOCAL_FAIL_ON_ERRORS;
    }
}
