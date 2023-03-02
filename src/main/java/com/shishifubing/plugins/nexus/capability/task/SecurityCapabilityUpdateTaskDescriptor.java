package com.shishifubing.plugins.nexus.capability.task;

import org.sonatype.nexus.scheduling.TaskDescriptorSupport;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SecurityCapabilityUpdateTaskDescriptor
        extends TaskDescriptorSupport {
    public static final String TYPE_ID = "security.task.update";
    public static final String TASK_NAME = "Security - update config from the remote source";

    public SecurityCapabilityUpdateTaskDescriptor() {
        super(TYPE_ID,
                SecurityCapabilityUpdateTask.class,
                TASK_NAME,
                VISIBLE,
                EXPOSED
        );
    }
}
