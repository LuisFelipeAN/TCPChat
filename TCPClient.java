import java.io.*; 
import java.net.*; 
import java.lang.Thread;

class TCPClient implements Runnable{ 
    Thread menu;
    private Escuta recebedor;
    private int PORTA;
    private String IP;
    private String IP_SERVIDOR;
    private int PORTA_SERVIDOR;
    
    
    public static void main(String argv[]) throws Exception 
    { 
        String ip;
        int portaEscuta;
        boolean instanciada = false;


        while(!instanciada){
            try{

                 System.out.println("Digite a porta de escuta e o endereco ip: PORTA,IP_SERVIDOR,PORTA_SERVIDOR"); 
                 BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                 String imput = inFromUser.readLine();
                 String [] parametros = imput.split(",");
                 TCPClient cliente = new TCPClient(InetAddress.getByName("localhost").getHostAddress(),Integer.parseInt(parametros[0]),parametros[1],Integer.parseInt(parametros[2]));
                 System.out.println("Cliente iniciado em -> IP: " + InetAddress.getByName("localhost").getHostAddress()+ " Porta: "+parametros[0]);
                 instanciada = true;

            }catch(SocketException se){System.out.println("Porta informada esta ocupada");}
            catch(IOException io){}
        }

        
            
    }
    public TCPClient(String IP,int PORTA, String IP_SERVIDOR, int PORTA_SERVIDOR){

        this.IP=IP;
        this.PORTA=PORTA;
        this.IP_SERVIDOR=IP_SERVIDOR;
        this.PORTA_SERVIDOR=PORTA_SERVIDOR;

        menu =  new Thread(this);
        menu.start();
        recebedor = new Escuta(this);

    }

    @Override
    public void run(){
        try{
            while(true){
                enviaMensagem(leMensagem());
            }
        }catch(Exception e){}
    }

    public String leMensagem()  throws Exception{
       

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        return inFromUser.readLine(); 
 
    }
    public void enviaMensagemEsperaResposta(String menssagem) throws Exception{
        String modifiedSentence; 
        Socket clientSocket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR); 

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 


        outToServer.writeBytes(menssagem + '\n'); 

        modifiedSentence = inFromServer.readLine(); 

        System.out.println("FROM SERVER: " + modifiedSentence); 

        clientSocket.close();
    }

    public void enviaMensagem(String mensagem) throws Exception{
        Socket clientSocket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR); 
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        outToServer.writeBytes(mensagem+ '\n'); 
        clientSocket.close();
    }

    public synchronized int getPortaCliente(){return this.PORTA;}
    public synchronized String getIPCliente(){return this.IP;}

    public class Escuta implements Runnable{
        private TCPClient tcpCliente;
        private Thread escuta;
        public Escuta(TCPClient tcpCliente){
            this.tcpCliente=tcpCliente;
            escuta = new Thread(this);
            escuta.start();
        }

        @Override
        public void run(){
            try{
                recebeMensagens();
            }catch(Exception e){}
        }

        public void recebeMensagens() throws Exception  {
          String menssagenRecebida; 
          ServerSocket welcomeSocket = new ServerSocket(tcpCliente.getPortaCliente()); 
      
          while(true) { 
      
                Socket connectionSocket = welcomeSocket.accept(); 

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 

                 menssagenRecebida = inFromClient.readLine(); 
                 System.out.println(menssagenRecebida); 
            } 
        }
    }
} 

        
