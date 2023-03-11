package project.pet.PostFlow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class QueueRepositoryTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private QueueRepository queueRepository;

    @Test
    public void testGetId() {
        Queue queue = new Queue();
        queue.setId(1L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertEquals(Long.valueOf(1), queue.getId());
    }

    @Test
    public void testSetId() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertEquals(Long.valueOf(2), queue.getId());
    }

    @Test
    public void testIsPriorityClient() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertTrue(queue.getPriorityClient());
    }

    @Test
    public void testSetPriorityClient() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(false);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertFalse(queue.getPriorityClient());
    }

    @Test
    public void testGetNextQueueNumber() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertEquals(Integer.valueOf(1), queue.getNextQueueNumber());
    }

    @Test
    public void testSetNextQueueNumber() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(2);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertEquals(Integer.valueOf(2), queue.getNextQueueNumber());
    }

    @Test
    public void testGetCurrentRequest() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertNotNull(queue.getCurrentRequest());
        assertEquals(Long.valueOf(1), queue.getCurrentRequest().getId());
    }

    @Test
    public void testSetCurrentRequest() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(2L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);

        assertEquals(Long.valueOf(2), queue.getCurrentRequest().getId());
    }

    @Test
    public void testGetRequests() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        Request request = new Request();
        request.setId(1L);
        queue.setCurrentRequest(request);
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        queue.setRequests(requests);
        assertNotNull(queue.getRequests());
        assertEquals(1, queue.getRequests().size());
        assertEquals(Long.valueOf(1), queue.getRequests().get(0).getId());
    }

    @Test
    public void testSetRequests() {
        Queue queue = new Queue();
        queue.setId(2L);
        queue.setPriorityClient(true);
        queue.setNextQueueNumber(1);
        List<Request> requests = new ArrayList<>();
        Request request = new Request();
        request.setId(2L);
        requests.add(request);
        queue.setRequests(requests);
        queue.setCurrentRequest(request);
        assertEquals(1, queue.getRequests().size());
        assertEquals(Long.valueOf(2), queue.getRequests().get(0).getId());
    }
}
