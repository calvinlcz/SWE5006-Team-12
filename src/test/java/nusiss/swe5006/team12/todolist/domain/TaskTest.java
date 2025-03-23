package nusiss.swe5006.team12.todolist.domain;

import static nusiss.swe5006.team12.todolist.domain.NotificationTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.TaskTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.WorkSpaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nusiss.swe5006.team12.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void notificationTest() {
        Task task = getTaskRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        task.addNotification(notificationBack);
        assertThat(task.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTask()).isEqualTo(task);

        task.removeNotification(notificationBack);
        assertThat(task.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTask()).isNull();

        task.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(task.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTask()).isEqualTo(task);

        task.setNotifications(new HashSet<>());
        assertThat(task.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTask()).isNull();
    }

    @Test
    void workSpaceTest() {
        Task task = getTaskRandomSampleGenerator();
        WorkSpace workSpaceBack = getWorkSpaceRandomSampleGenerator();

        task.setWorkSpace(workSpaceBack);
        assertThat(task.getWorkSpace()).isEqualTo(workSpaceBack);

        task.workSpace(null);
        assertThat(task.getWorkSpace()).isNull();
    }
}
