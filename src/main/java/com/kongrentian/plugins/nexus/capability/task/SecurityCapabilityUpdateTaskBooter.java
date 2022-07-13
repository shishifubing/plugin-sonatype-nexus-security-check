package com.kongrentian.plugins.nexus.capability.task;


import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.scheduling.TaskConfiguration;
import org.sonatype.nexus.scheduling.TaskInfo;
import org.sonatype.nexus.scheduling.TaskRemovedException;
import org.sonatype.nexus.scheduling.TaskScheduler;
import org.sonatype.nexus.scheduling.schedule.Cron;
import org.sonatype.nexus.scheduling.schedule.Schedule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.kongrentian.plugins.nexus.capability.task.SecurityCapabilityUpdateTaskDescriptor.TASK_NAME;
import static org.sonatype.nexus.common.app.ManagedLifecycle.Phase.TASKS;

@Named
@Singleton
@ManagedLifecycle(phase = TASKS)
public class SecurityCapabilityUpdateTaskBooter
        extends LifecycleSupport {

    // every five minutes
    static final String CRON = "*/5 * * * * ?";

    private final TaskScheduler taskScheduler;

    @Inject
    public SecurityCapabilityUpdateTaskBooter(final TaskScheduler taskScheduler) {
        this.taskScheduler = checkNotNull(taskScheduler);
    }

    private static Predicate<TaskInfo> isUpdateTask() {
        return info -> SecurityCapabilityUpdateTaskDescriptor.TYPE_ID
                .equals(info.getConfiguration().getTypeId());
    }

    @Override
    protected void doStart() {
        createUpdateTask();
    }

    private void createUpdateTask() {
        if (!doesTaskExist()) {
            TaskConfiguration taskConfig = taskScheduler
                    .createTaskConfigurationInstance(
                            SecurityCapabilityUpdateTaskDescriptor.TYPE_ID);
            taskConfig.setName(TASK_NAME);
            try {
                Cron cron = taskScheduler
                        .getScheduleFactory()
                        .cron(new Date(), CRON);
                taskScheduler.scheduleTask(taskConfig, cron);
            } catch (RuntimeException exception) {
                log.error("Problem scheduling update task",
                        exception);
            }
        }
        removeDuplicates();
    }

    private void removeDuplicates() {
        List<TaskInfo> tasks = taskScheduler
                .listsTasks()
                .stream()
                .filter(isUpdateTask())
                .filter(info -> TASK_NAME.equals(
                        info.getConfiguration().getName()))
                .filter(scheduleMatches())
                .collect(Collectors.toList());

        if (tasks.size() == 0) {
            return;
        }
        tasks.subList(1, tasks.size())
                .forEach(TaskInfo::remove);
        try {
            tasks.get(0).runNow();
        } catch (TaskRemovedException exception) {
            log.error("Could not run the update task on startup", exception);
        }
    }

    private boolean doesTaskExist() {
        return taskScheduler
                .listsTasks()
                .stream()
                .anyMatch(isUpdateTask());
    }

    private Predicate<TaskInfo> scheduleMatches() {
        Cron cron = taskScheduler.getScheduleFactory().cron(new Date(), CRON);
        return taskInfo -> {
            Schedule schedule = taskInfo.getSchedule();
            return schedule instanceof Cron
                    && cron.getCronExpression()
                    .equals(((Cron) schedule)
                            .getCronExpression());
        };
    }
}
