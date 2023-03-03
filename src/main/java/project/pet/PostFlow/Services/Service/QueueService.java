package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;

import java.util.List;

public interface QueueService {

    int AVERAGE_WAITING_TIME_IN_MINUTES = 5;

    default int getAverageWaitingTimeInMinutes() {
        return AVERAGE_WAITING_TIME_IN_MINUTES;
    }
    Request addRequest(Client client, RequestType requestType, String appointmentTime); //добавляю заявку в очередь на обслуживание и возвращаю добавленную заявку
    Request getCurrentRequest(); //возвращаю текущую заявку из очереди на обслуживание
    void markCurrentRequestDone(); //помечаю текущую заявку как выполненную
    List<Request> getRequests(); //возвращаю список всех заявок в очереди на обслуживание
    void removeRequest(Request request); //удаляю заданную заявку из очереди на обслуживание
    void recalculateEstimatedTime(); //пересчитываю ожидаемое время обслуживания для всех заявок в очереди
    void removeFromQueue(Client client); //удаляю клиента из обычной очереди
    List<Client> getClientQueue(); //возвращаю список клиентов в обычной очереди

}
