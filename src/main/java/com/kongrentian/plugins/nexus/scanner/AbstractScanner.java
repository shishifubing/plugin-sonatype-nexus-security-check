package com.kongrentian.plugins.nexus.scanner;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import com.kongrentian.plugins.nexus.capability.form_fields.SecurityCapabilityKey;
import com.kongrentian.plugins.nexus.model.request_information.RequestInformation;
import com.kongrentian.plugins.nexus.model.scan_result.ScanResult;
import com.kongrentian.plugins.nexus.model.scan_result.ScanResultType;
import org.sonatype.nexus.repository.storage.AssetStore;

import static com.kongrentian.plugins.nexus.logging.SecurityLogConfiguration.LOG;

abstract public class AbstractScanner {

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

    abstract public SecurityCapabilityKey getFailKey();

    public boolean failOnErrors() {
        return (Boolean) securityCapabilityHelper
                .getCapabilityConfiguration()
                .get(getFailKey());
    }
}
