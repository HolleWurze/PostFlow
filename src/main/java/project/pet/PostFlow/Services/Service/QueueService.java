package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueService {
    Request addRequest(Client client, RequestType requestType, LocalDateTime appointmentTime); //добавляет заявку в очередь на обслуживание и возвращает добавленную заявку
    Request getCurrentRequest(); //возвращает текущую заявку из очереди на обслуживание
    void markCurrentRequestDone(); //помечает текущую заявку как выполненную
    List<Request> getRequests(); //возвращает список всех заявок в очереди на обслуживание
    void removeRequest(Request request); //удаляет заданную заявку из очереди на обслуживание
    void recalculateEstimatedTime(); //пересчитывает ожидаемое время обслуживания для всех заявок в очереди
    void removeFromQueue(Client client); //удаляет клиента из обычной очереди
    List<Client> getClientQueue(); //возвращает список клиентов в обычной очереди
}
