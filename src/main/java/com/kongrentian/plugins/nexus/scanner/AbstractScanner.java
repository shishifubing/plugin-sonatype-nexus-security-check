package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.MonitoringInformationScanResult;
import com.kongrentian.plugins.nexus.model.RequestInformation;
import com.kongrentian.plugins.nexus.model.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.repository.storage.AssetStore;

abstract public class AbstractScanner {
    static final Logger LOG = LoggerFactory.getLogger(AbstractScanner.class);

    final AssetStore assetStore;
    final SecurityCapabilityHelper securityCapabilityHelper;

    public AbstractScanner(final AssetStore assetStore,
                           final SecurityCapabilityHelper securityCapabilityHelper) {
        this.assetStore = assetStore;
        this.securityCapabilityHelper = securityCapabilityHelper;
    }

    public ScanResult scan(RequestInformation information) {
        Boolean allowed = (Boolean) securityCapabilityHelper
                .getCapabilityConfiguration()
                .get(failKey());
        String message = "There was an exception and "
                + failKey().propertyKey() + " is " + !allowed;
        try {
            return scanImpl(information);
        } catch (Throwable exception) {
            return new ScanResult(allowed, message, exception);
        }
    }

    abstract ScanResult scanImpl(RequestInformation information) throws Throwable;

    abstract public SecurityCapabilityKey failKey();

    public MonitoringInformationScanResult asResult(ScanResult result) {
        return new MonitoringInformationScanResult(failKey(), result);
    }
}
