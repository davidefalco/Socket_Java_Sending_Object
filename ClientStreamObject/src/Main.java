import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Main {
    public static void main(String[] args) throws IOException {
        InetAddress localIP;
        if(args.length == 0){
            /* Non è stato passato alcun indirizzo da args: client e server stanno sulla stessa macchina. */
            localIP = InetAddress.getByName(null);
        }else{
            localIP = InetAddress.getByName(args[0]);
        }

        Socket socket = null;
        InputStream inStream = null;
        OutputStream outStream = null;

        Email mail = null;

        try{
            socket = new Socket(localIP, 8189);
            System.out.println("Client started");
            System.out.println("Client socket: " + socket);

            /* Creazione stream di output per la socket. Dato che voglio inviare un oggetto sullo stream
             * della socket uso un ObjectOutputStream. */
            outStream = socket.getOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(outStream);
            System.out.println("Sono qua1");

            mail = new Email("davidefalco@email.com", "francesco@mail.com", "Invio oggetti sulla socket", "Speriamo che funziona");
            System.out.println("Voglio inviare: " + mail.toString());

            int i = 0;
            while(i != 1) {
                System.out.println("Invio la mail.. ");

                /* Scrivo l'oggetto mail sullo stream di output. */
                outputStream.writeObject(mail);
                i++;

                /* Creazione stream di input per la socket. Dato che voglio ricevere un oggetto sullo stream
                 * della socket uso un ObjectInputStream. */
                inStream = socket.getInputStream();
                ObjectInputStream inputStream = new ObjectInputStream(inStream);

                /* Ricevo l'oggetto dallo stream di output, aspettando una conferma. */
                Email conferma = (Email) inputStream.readObject();
                if (conferma.getFrom().compareTo("OK") == 0) {
                    System.out.println("La mail è stata ricevuta correttamente dal server");
                }

                Email closeConnection = new Email("CLOSE", null, null, null);
                outputStream.flush();
                outputStream.writeObject(closeConnection);
            }
        } catch (UnknownHostException e){
            System.err.println("Don’t know about host "+ localIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn’t get I/O for the connection to: " + localIP);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn’t get Email, object received is not an Email type" + localIP);
            System.exit(1);
        }

        System.out.println("Client: closing...");
        inStream.close();
        outStream.close();
        socket.close();
    }
}