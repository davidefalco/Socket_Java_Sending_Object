import java.io.*;
import java.net.*;

public class Main {
    public static final int port = 8189;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        System.out.println("Socket: " + serverSocket);

        Socket clientSocket = null;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try{
            /*
             * Finchè non avviene una connessione il server resta in attesa su questa riga di codice.
             * Quando un client tenterà di connettersi, allora vuol dire che il server avrà accettato qualcosa
             * e quindi ritorna la socket del client che ha appena accettato.
             * */
            clientSocket = serverSocket.accept();
            System.out.println("Connection accepted from: " + clientSocket);

            /* Creazione stream di input da clientSocket. Uso ObjectInputStream perchè voglio ricevere oggetti. */
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            /* Creazione stream di output su clientSocket. Uso ObjectOutputStream perchè voglio inviare oggetti. */
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            /* Funzione effettiva del server: quando ricevo una mail rispondo con una mail vuota il cui from è OK */
            while(true){
                Email mailRcv = (Email) inputStream.readObject();
                /* Se il cast non funziona viene lanciata una eccezione ClassNotFound che viene cattuarata (secondo catch). */
                System.out.println("Message received: " + mailRcv);
                if(mailRcv != null){
                    /* Invio conferma in cui la from è OK. */
                    Email conferma = new Email("OK", null, null, null);
                    outputStream.writeObject(conferma);
                    if(mailRcv.getFrom().compareTo("CLOSE") == 0)
                        break;
                }
            }

        }catch (IOException e){
            System.err.println("Accept failed");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn’t get Email, object received is not an Email type");
            System.exit(1);
        }

        /* Chiusura stream e socket. */
        inputStream.close();
        outputStream.close();
        serverSocket.close();
        clientSocket.close();
    }
}