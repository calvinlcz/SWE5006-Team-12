package nusiss.swe5006.team12.todolist.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WorkSpaceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WorkSpace getWorkSpaceSample1() {
        return new WorkSpace().id(1L).name("name1").createdBy("createdBy1").modifiedBy("modifiedBy1");
    }

    public static WorkSpace getWorkSpaceSample2() {
        return new WorkSpace().id(2L).name("name2").createdBy("createdBy2").modifiedBy("modifiedBy2");
    }

    public static WorkSpace getWorkSpaceRandomSampleGenerator() {
        return new WorkSpace()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .modifiedBy(UUID.randomUUID().toString());
    }
}
