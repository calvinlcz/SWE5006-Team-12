package nusiss.swe5006.team12.todolist.service;

import nusiss.swe5006.team12.todolist.domain.Task;

public interface IOAuth2StrategyService {
    void addEvent(Task task) throws Exception;
}
