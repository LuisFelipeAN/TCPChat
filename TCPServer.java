import java.io.*; 
import java.net.*; 
import java.lang.Thread;
import java.util.ArrayList;
///codigo do pedido, portaResposta ,codigo do cliente, mensagem


class TCPServer implements Runnable { 
  private Thread server;
  private int porta;
  private ArrayList<Cliente> clientes;
  public static void main(String argv[]) throws Exception 
    { 

     TCPServer tcpServer = new TCPServer();
     
    }
    public TCPServer(){
    	porta = 6789;
    	try{
    		System.out.println("Servidor iniciado em -> IP: " + InetAddress.getByName("localhost").getHostAddress()+ " Porta: "+porta);
    	}catch(Exception e){}
    	clientes = new ArrayList<>();
        server = new Thread(this);
     	server.start();
    };

    @Override
    public void run(){
    	try{
    		rodaServidor();	
    	}catch(Exception e){}
      
    }

    public void rodaServidor() throws Exception {

      String clientSentence; 
      ServerSocket welcomeSocket = new ServerSocket(porta); 
      while(true){ 
  
            Socket connectionSocket = welcomeSocket.accept(); 
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
            clientSentence = inFromClient.readLine(); 
            new Thread(new TratadorRequisicoes(this,clientSentence,connectionSocket.getInetAddress().getHostAddress())).start();

        } 
    }

    public synchronized void adicionaCliente(Cliente cliente){
    	clientes.add(cliente);
    }
    public synchronized Cliente getCliente(String identificador){
    	return null;
    }

    public class Cliente{
    	private String identificador;
    	private String IP;
    	private int PORTA;
    	private boolean disponivel;
    }

    public class TratadorRequisicoes implements Runnable{
    	private TCPServer servidor;
    	private Mensagem menssagem;
    	private String enderecoOrigem;

    	public TratadorRequisicoes(TCPServer servidor,String menssagem,String enderecoOrigem){
    		this.servidor = servidor;
    		this.menssagem= new Mensagem(menssagem);
    		this.enderecoOrigem=enderecoOrigem;
    	}

    	@Override
    	public void run(){
    		String capitalizedSentence; 
    		capitalizedSentence = menssagem.conteudo.toUpperCase() + '\n'; 
    		try{
               enviaMensagen(enderecoOrigem,menssagem.portaOrigem,capitalizedSentence);
            }catch(Exception e){}
    	}

    	public void enviaMensagen(String endereco,int porta, String menssagem) throws Exception{
        String modifiedSentence; 
        Socket clientSocket = new Socket(endereco, porta); 
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        outToServer.writeBytes(menssagem + '\n'); 
        clientSocket.close();

       
    }
     public class Mensagem{
        	public String codigo;
        	public int portaOrigem;
        	public String destinatario;
        	public String conteudo;

        	public Mensagem(String menssagem){
        		String [] aux = menssagem.split(",");
        		codigo = aux[0];
        		portaOrigem =  Integer.parseInt(aux[1]);
        		destinatario = aux[2];
        		conteudo = aux[3];
        	}

        }

    }
} 
 
