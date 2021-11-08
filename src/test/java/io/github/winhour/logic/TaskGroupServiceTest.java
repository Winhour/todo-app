package io.github.winhour.logic;

import io.github.winhour.model.TaskGroup;
import io.github.winhour.model.TaskGroupRepository;
import io.github.winhour.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException if existsByDoneIsFalseAndGroup_Id returns true")
    void toggleGroup_ifExistsReturnsTrue_throwsIllegalStateException() {

        //given

        var mockRepository = mock(TaskRepository.class);
        when(mockRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);

        //system under test

        var toTest = new TaskGroupService(null, mockRepository);

        //when

        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        //then

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException if Task Group with given id is not found")
    void toggleGroup_ifTaskGroupWithGivenIdIsNotFound_throwsIllegalArgumentException() {

        //given

        var mockRepository = mock(TaskRepository.class);
        when(mockRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when (mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test

        var toTest = new TaskGroupService(mockGroupRepository, mockRepository);

        //when

        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        //then

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TaskGroup with given id");

    }

    @Test
    @DisplayName("if everything went correctly should toggle done status")
    void toggleGroup_ifEverythingIsAlright_toggleDoneStatus() {

        //given

        TaskGroup group = new TaskGroup();
        var startToggle = group.isDone();

        var mockRepository = mock(TaskRepository.class);
        when(mockRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when (mockGroupRepository.findById(anyInt())).thenReturn(Optional.ofNullable(group));

        //system under test

        var toTest = new TaskGroupService(mockGroupRepository, mockRepository);

        //when

        toTest.toggleGroup(1);

        //then

        var endToggle = group.isDone();

        assertThat(startToggle).isNotEqualTo(endToggle);


    }



}