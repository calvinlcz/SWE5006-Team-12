package nusiss.swe5006.team12.todolist.domain;

import static nusiss.swe5006.team12.todolist.domain.RegisteredTestSamples.*;
import static nusiss.swe5006.team12.todolist.domain.WorkSpaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nusiss.swe5006.team12.todolist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegisteredTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registered.class);
        Registered registered1 = getRegisteredSample1();
        Registered registered2 = new Registered();
        assertThat(registered1).isNotEqualTo(registered2);

        registered2.setId(registered1.getId());
        assertThat(registered1).isEqualTo(registered2);

        registered2 = getRegisteredSample2();
        assertThat(registered1).isNotEqualTo(registered2);
    }

    @Test
    void workSpaceTest() {
        Registered registered = getRegisteredRandomSampleGenerator();
        WorkSpace workSpaceBack = getWorkSpaceRandomSampleGenerator();

        registered.addWorkSpace(workSpaceBack);
        assertThat(registered.getWorkSpaces()).containsOnly(workSpaceBack);
        assertThat(workSpaceBack.getRegistereds()).containsOnly(registered);

        registered.removeWorkSpace(workSpaceBack);
        assertThat(registered.getWorkSpaces()).doesNotContain(workSpaceBack);
        assertThat(workSpaceBack.getRegistereds()).doesNotContain(registered);

        registered.workSpaces(new HashSet<>(Set.of(workSpaceBack)));
        assertThat(registered.getWorkSpaces()).containsOnly(workSpaceBack);
        assertThat(workSpaceBack.getRegistereds()).containsOnly(registered);

        registered.setWorkSpaces(new HashSet<>());
        assertThat(registered.getWorkSpaces()).doesNotContain(workSpaceBack);
        assertThat(workSpaceBack.getRegistereds()).doesNotContain(registered);
    }
}
