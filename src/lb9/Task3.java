
package lb9;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class Task3 {

    static class Session {
        private String userName;
        private String sessionId;

        public Session(String userName) {
            this.userName = userName;
            this.sessionId = UUID.randomUUID().toString();
        }

        public String getUserName() {
            return userName;
        }

        public String getSessionId() {
            return sessionId;
        }

        @Override
        public String toString() {
            return "Пользователь: " + userName +
                    ", ID сессии: " + sessionId;
        }
    }

    static class SessionRegistry {

        private final ConcurrentHashMap<String, Session> sessions =
                new ConcurrentHashMap<>();

        public boolean createSession(String userName) {

            Session newSession = new Session(userName);

            Session previous = sessions.putIfAbsent(
                    userName, newSession
            );

            if (previous == null) {
                System.out.println("Сессия создана: " + userName);
                return true;
            } else {
                System.out.println("Сессия уже существует: " + userName);
                return false;
            }
        }

        public Session getSession(String userName) {
            return sessions.get(userName);
        }

        public void removeSession(String userName) {
            Session removed = sessions.remove(userName);

            if (removed != null) {
                System.out.println("Сессия удалена: " + userName);
            } else {
                System.out.println("Сессия не найдена: " + userName);
            }
        }

        public void showSessions() {
            System.out.println("\nАктивные сессии:");

            for (Session session : sessions.values()) {
                System.out.println(session);
            }

            System.out.println("Всего сессий: " + sessions.size());
        }
    }

    public static void main(String[] args) throws InterruptedException {

        SessionRegistry registry = new SessionRegistry();

        registry.createSession("Ivan");
        registry.createSession("Petr");
        registry.createSession("Ivan");

        registry.showSessions();

        System.out.println("\nПроверка многопоточности:");

        Thread thread1 = new Thread(() ->
                registry.createSession("Alex"));

        Thread thread2 = new Thread(() ->
                registry.createSession("Alex"));

        Thread thread3 = new Thread(() ->
                registry.createSession("Alex"));

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        registry.showSessions();

        System.out.println("\nПоиск сессии:");

        Session session = registry.getSession("Ivan");

        if (session != null) {
            System.out.println(session);
        }

        System.out.println("\nУдаление:");

        registry.removeSession("Petr");

        registry.showSessions();
    }
}
