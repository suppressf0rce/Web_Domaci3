package server;

import model.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ServerThread implements Runnable {

    private List<Contact> addressBook;
    private Socket socket;

    public ServerThread(List<Contact> addressBook, Socket socket) {
        this.addressBook = addressBook;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

            //Gson gson = new Gson();

            String header = in.readLine();
            System.out.println(header);
            while(!in.readLine().trim().equals(""));

            int type = checkHeader(header);

            if(type == 1)
                addContact(header);

            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write(getPage(type));
            out.flush();

            out.close();
            in.close();
            socket.close();
            System.out.println("Disconnecting Client");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getPage(int type){

        try {
            String result = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("index.html").toURI())), StandardCharsets.UTF_8);

            if(type==0){
                return result +"</body></html>";
            }

            if(type==1){
                return result + "<p>Contact Added</p></body></html>";
            }

            if(type==2){
                StringBuilder builder = new StringBuilder();
                addressBook.forEach(contact -> {
                    builder.append("<p>");
                    builder.append(contact.toString());
                    builder.append("</p>");
                });
                result += builder.toString();
                return result +"</body></html>";

            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int checkHeader(String header){

        if(header.contains("add")){
            return 1;
        }

        if(header.contains("list")){
            return 2;
        }

        return 0;

    }

    private void addContact(String header){
        int tmp = header.indexOf("firstname=");
        String firstName = header.substring(tmp+10,header.length());
        tmp = firstName.indexOf('&');
        firstName = firstName.substring(0, tmp);

        tmp = header.indexOf("lastname=");
        String lastName = header.substring(tmp+9, header.length());
        tmp = lastName.indexOf('&');
        lastName = lastName.substring(0,tmp);

        tmp = header.indexOf("phonenumber=");
        String phoneNumber = header.substring(tmp+12, header.length());
        tmp = phoneNumber.indexOf('&');
        phoneNumber = phoneNumber.substring(0,tmp);
        Contact contact = new Contact.Builder().firstName(firstName).lastName(lastName).phoneNumber(phoneNumber).build();

        addressBook.add(contact);
    }

}