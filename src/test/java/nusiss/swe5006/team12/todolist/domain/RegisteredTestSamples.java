package nusiss.swe5006.team12.todolist.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegisteredTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Registered getRegisteredSample1() {
        return new Registered().id(1L).accountNo("accountNo1").linkWorkspace("linkWorkspace1");
    }

    public static Registered getRegisteredSample2() {
        return new Registered().id(2L).accountNo("accountNo2").linkWorkspace("linkWorkspace2");
    }

    public static Registered getRegisteredRandomSampleGenerator() {
        return new Registered()
            .id(longCount.incrementAndGet())
            .accountNo(UUID.randomUUID().toString())
            .linkWorkspace(UUID.randomUUID().toString());
    }
}
