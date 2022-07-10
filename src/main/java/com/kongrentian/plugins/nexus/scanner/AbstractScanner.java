package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.RequestInformation;
import com.kongrentian.plugins.nexus.model.ScanResult;
import com.kongrentian.plugins.nexus.model.ScanResultType;
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
        try {
            return scanImpl(information);
        } catch (Throwable exception) {
            boolean fail = failOnErrors();
            return new ScanResult(
                    !fail,
                    fail ? ScanResultType.EXCEPTION
                            : ScanResultType.EXCEPTION_IGNORED)
                    .setException(exception);
        }
    }

    abstract ScanResult scanImpl(RequestInformation information) throws Throwable;

    abstract public SecurityCapabilityKey getFailKey();

    public boolean failOnErrors() {
        return (Boolean) securityCapabilityHelper
                .getCapabilityConfiguration()
                .get(getFailKey());
    }
}
