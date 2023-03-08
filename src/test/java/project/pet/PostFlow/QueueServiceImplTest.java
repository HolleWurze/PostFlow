package project.pet.PostFlow;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.DTO.RequestDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Services.Service.RequestService;
import project.pet.PostFlow.Services.ServiceImpl.QueueServiceImpl;
import project.pet.PostFlow.Services.ServiceImpl.RequestServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QueueServiceImplTest {

    //    @Spy
    private ModelMapper modelMapper;
    //    @Mock
    private QueueRepository queueRepository;
    private int averageWaitingTimeInMinutes;
    //    @Mock
    private RequestService requestService;
    //    @InjectMocks
    private QueueServiceImpl queueService;

}
