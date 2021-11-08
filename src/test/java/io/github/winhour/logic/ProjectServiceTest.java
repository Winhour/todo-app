package io.github.winhour.logic;

import io.github.winhour.TaskConfigurationProperties;
import io.github.winhour.model.*;
import io.github.winhour.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_openGroupExists_throwsIllegalStateException() {

        //given

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        //system under test

        var toTest = new ProjectService(null, mockConfig, mockGroupRepository, null);

        /*
        try {
            //when

            toTest.createGroup(0, LocalDateTime.now());
        } catch (IllegalStateException e){
            //then

            assertThat(e).isEqualTo(IllegalStateException.class);

        }*/

        //when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");

        /*
        assertThatThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class);

        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));
        */


        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(1));

    }

    /*****************************************************************************************************************************************/

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just one group and there are no groups and no projects for a given id")
    void createGroup_configurationOK_And_No_Projects_throwsIllegalArgumentException() {

        //given

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        var mockRepository = mock(ProjectRepository.class);
        when (mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        //system under test

        var toTest = new ProjectService(mockRepository, mockConfig, null, null);


        //when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given projectId");


    }

    /*****************************************************************************************************************************************/

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration is ok and there are no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_NoUndoneGroupExists_And_No_Projects_throwsIllegalArgumentException() {

        //given

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(false);

        var mockRepository = mock(ProjectRepository.class);
        when (mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        //system under test

        var toTest = new ProjectService(mockRepository, mockConfig, mockGroupRepository, null);


        //when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given projectId");


    }

    /*****************************************************************************************************************************************/

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOK_existingProject_createsAndSavesGroup(){

        //given

        var today = LocalDate.now().atStartOfDay();

        var project = projectWith("bar", Set.of(-1, -2));

        var mockRepository = mock(ProjectRepository.class);
        when (mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroupRepo = new InMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();

        var serviceWithInMemRepo = new TaskGroupService(inMemoryGroupRepo, null);

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        //system under test

        var toTest = new ProjectService(mockRepository, mockConfig, inMemoryGroupRepo, serviceWithInMemRepo);

        //when

        GroupReadModel result = toTest.createGroup(1, today);

        //then

        assertThat(result)
                .hasFieldOrPropertyWithValue("description", "bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 2)
                .isEqualTo(inMemoryGroupRepo.count());

    }

    /*****************************************************************************************************************************************/

    private TaskGroupRepository InMemoryTaskRepository(){
        return new TaskGroupRepository() {

            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return map.values().stream().collect(Collectors.toList());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if(entity.getId() != 0){
                    map.put(entity.getId(), entity);
                } else {
                    entity.setId(++index);
                    map.put(index, entity);
                }
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(group -> !group.isDone())
                        .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
            }

            @Override
            public boolean existsById(Integer id) {
                return false;
            }

            @Override
            public List<TaskGroup> findAllByDoneIsFalseAndProject_Id(Integer projectId) {
                return null;
            }

            /*@Override
            public TaskRepository getTasks() {
                return null;
            }*/

        };

    }

    /*****************************************************************************************************************************************/

    private static class InMemoryGroupRepository implements TaskGroupRepository {

        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }


        @Override
        public List<TaskGroup> findAll() {
            return map.values().stream().collect(Collectors.toList());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if(entity.getId() != 0){
                map.put(entity.getId(), entity);
            } else {
                entity.setId(++index);
                map.put(index, entity);
            }
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }

        @Override
        public boolean existsById(Integer id) {
            return false;
        }

        @Override
        public List<TaskGroup> findAllByDoneIsFalseAndProject_Id(Integer projectId) {
            return null;
        }




    }

    /*****************************************************************************************************************************************/

    private TaskConfigurationProperties configurationReturning(final boolean b){

        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }


    /*****************************************************************************************************************************************/

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDays_to_deadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getProjectsteps()).thenReturn(steps);
        return result;
    }


}