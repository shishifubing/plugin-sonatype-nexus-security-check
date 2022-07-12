package com.kongrentian.plugins.nexus.capability.task;

import org.sonatype.nexus.blobstore.api.BlobStoreManager;
import org.sonatype.nexus.blobstore.api.BlobStoreUsageChecker;
import org.sonatype.nexus.scheduling.Cancelable;
import org.sonatype.nexus.scheduling.TaskSupport;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

@Named
public class SecurityCapabilityUpdateTask extends TaskSupport
        implements Cancelable {


    private final BlobStoreManager blobStoreManager;

    private final BlobStoreUsageChecker blobStoreUsageChecker;

    @Inject
    public SecurityCapabilityUpdateTask(final BlobStoreManager blobStoreManager,
                                        final BlobStoreUsageChecker blobStoreUsageChecker) {
        this.blobStoreManager = checkNotNull(blobStoreManager);
        this.blobStoreUsageChecker = checkNotNull(blobStoreUsageChecker);
    }

    @Override
    protected Object execute() throws Exception {
        return null;
    }

    @Override
    public String getMessage() {
        return "Compacting blob store";
    }

}
