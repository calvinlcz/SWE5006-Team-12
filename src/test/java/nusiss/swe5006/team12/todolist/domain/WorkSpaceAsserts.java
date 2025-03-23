package nusiss.swe5006.team12.todolist.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkSpaceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkSpaceAllPropertiesEquals(WorkSpace expected, WorkSpace actual) {
        assertWorkSpaceAutoGeneratedPropertiesEquals(expected, actual);
        assertWorkSpaceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkSpaceAllUpdatablePropertiesEquals(WorkSpace expected, WorkSpace actual) {
        assertWorkSpaceUpdatableFieldsEquals(expected, actual);
        assertWorkSpaceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkSpaceAutoGeneratedPropertiesEquals(WorkSpace expected, WorkSpace actual) {
        assertThat(actual)
            .as("Verify WorkSpace auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkSpaceUpdatableFieldsEquals(WorkSpace expected, WorkSpace actual) {
        assertThat(actual)
            .as("Verify WorkSpace relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getDateCreated()).as("check dateCreated").isEqualTo(expected.getDateCreated()))
            .satisfies(a -> assertThat(a.getCreatedBy()).as("check createdBy").isEqualTo(expected.getCreatedBy()))
            .satisfies(a -> assertThat(a.getDateModified()).as("check dateModified").isEqualTo(expected.getDateModified()))
            .satisfies(a -> assertThat(a.getModifiedBy()).as("check modifiedBy").isEqualTo(expected.getModifiedBy()))
            .satisfies(a -> assertThat(a.getSharedWorkspace()).as("check sharedWorkspace").isEqualTo(expected.getSharedWorkspace()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkSpaceUpdatableRelationshipsEquals(WorkSpace expected, WorkSpace actual) {
        assertThat(actual)
            .as("Verify WorkSpace relationships")
            .satisfies(a -> assertThat(a.getRegistereds()).as("check registereds").isEqualTo(expected.getRegistereds()));
    }
}
