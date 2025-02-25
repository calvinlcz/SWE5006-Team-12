package nusiss.swe5006.team12.todolist.domain;

import static nusiss.swe5006.team12.todolist.domain.NotificationTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nusiss.swe5006.team12.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void taskTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        notification.setTask(taskBack);
        assertThat(notification.getTask()).isEqualTo(taskBack);

        notification.task(null);
        assertThat(notification.getTask()).isNull();
    }
}
