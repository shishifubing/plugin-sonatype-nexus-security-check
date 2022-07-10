package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityConfiguration;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.RequestInformation;
import com.kongrentian.plugins.nexus.model.ScanResult;
import com.kongrentian.plugins.nexus.model.ScanResultType;
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
        if (scanResultType != null) {
            return new ScanResult(true, scanResultType);
        }
        DateTime lastModified = information.getComponent().getLastModified();
        if (lastModified.isBefore(config.getScanLocalLastModified())) {
            return new ScanResult(true, ScanResultType.VALID_LAST_MODIFIED);
        }
        return new ScanResult(false, ScanResultType.INVALID_LAST_MODIFIED);
    }

    @Override
    public SecurityCapabilityKey getFailKey() {
        return SecurityCapabilityKey.SCAN_LOCAL_FAIL_ON_ERRORS;
    }
}
