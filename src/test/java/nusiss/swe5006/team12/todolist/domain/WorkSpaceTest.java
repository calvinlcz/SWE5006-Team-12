package nusiss.swe5006.team12.todolist.domain;

import static nusiss.swe5006.team12.todolist.domain.RegisteredTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.TaskTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.WorkSpaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nusiss.swe5006.team12.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkSpaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkSpace.class);
        WorkSpace workSpace1 = getWorkSpaceSample1();
        WorkSpace workSpace2 = new WorkSpace();
        assertThat(workSpace1).isNotEqualTo(workSpace2);

        workSpace2.setId(workSpace1.getId());
        assertThat(workSpace1).isEqualTo(workSpace2);

        workSpace2 = getWorkSpaceSample2();
        assertThat(workSpace1).isNotEqualTo(workSpace2);
    }

    @Test
    void taskTest() {
        WorkSpace workSpace = getWorkSpaceRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        workSpace.addTask(taskBack);
        assertThat(workSpace.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getWorkSpace()).isEqualTo(workSpace);

        workSpace.removeTask(taskBack);
        assertThat(workSpace.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getWorkSpace()).isNull();

        workSpace.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(workSpace.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getWorkSpace()).isEqualTo(workSpace);

        workSpace.setTasks(new HashSet<>());
        assertThat(workSpace.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getWorkSpace()).isNull();
    }

    @Test
    void registeredTest() {
        WorkSpace workSpace = getWorkSpaceRandomSampleGenerator();
        Registered registeredBack = getRegisteredRandomSampleGenerator();

        workSpace.addRegistered(registeredBack);
        assertThat(workSpace.getRegistereds()).containsOnly(registeredBack);

        workSpace.removeRegistered(registeredBack);
        assertThat(workSpace.getRegistereds()).doesNotContain(registeredBack);

        workSpace.registereds(new HashSet<>(Set.of(registeredBack)));
        assertThat(workSpace.getRegistereds()).containsOnly(registeredBack);

        workSpace.setRegistereds(new HashSet<>());
        assertThat(workSpace.getRegistereds()).doesNotContain(registeredBack);
    }
}
