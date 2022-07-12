package com.kongrentian.plugins.nexus.capability.task;

import com.kongrentian.plugins.nexus.capability.SecurityCapabilityHelper;
import org.sonatype.nexus.scheduling.Cancelable;
import org.sonatype.nexus.scheduling.TaskSupport;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SecurityCapabilityUpdateTask extends TaskSupport
        implements Cancelable {

    private final SecurityCapabilityHelper securityCapabilityHelper;

    @Inject
    public SecurityCapabilityUpdateTask(final SecurityCapabilityHelper securityCapabilityHelper) {
        this.securityCapabilityHelper = securityCapabilityHelper;
    }

    @Override
    protected Object execute() throws Exception {
        return null;
    }

    @Override
    public String getMessage() {
        return "message";
    }

}
