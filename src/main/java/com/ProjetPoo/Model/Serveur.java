package com.ProjetPoo.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.ProjetPoo.ModelSlither.SlitherData;
import com.ProjetPoo.ModelSlither.TeteSlither;
import com.ProjetPoo.ModelSlither.NourritureSlither;
import com.ProjetPoo.ModelSlither.PointSerialisable;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light.Point;

public class Serveur extends Thread {
    private ServerSocket serverSocket;
    private LinkedList<ClientHandler> clients = new LinkedList<>();

    //garde la nourriture en memoire afin de l'envoyer aux nouveaux joueurs, limite generation à 1000
    private LinkedList<PointSerialisable> food  = new LinkedList<>();
 
    private int port;

    public Serveur(int port) {
        this.port=port;
    }

    //thread d'entree des joueurs
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //food.add(new PointSerialisable(new Point2D(200, 200)));
        System.out.println("serveur lancé");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client trouvé");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                //sendId(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                System.out.println("Erreur client : " + e.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }

    //thread pool
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private SlitherData data;
        private ObjectInputStream in;
        private long id;
        private boolean baseFoodSent = false;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                Object obj;
                while ((obj = in.readObject()) != null) {
                    //recup coord pointeur
                    data = (SlitherData) obj;
                    //cas d'un cadavre (mort)
                    if (!data.getFoodDead().isEmpty()) {
                        for (PointSerialisable ps : data.getFoodDead()) {
                            food.add(ps);
                        }
                    }
                    if (data.getFoodAccelere()!=null) {
                        food.add(data.getFoodAccelere());
                    }
                    if (data.getFoodEaten()!=null) {
                        PointSerialisable nourriture = data.getFoodEaten();
                        System.out.println("nourriture mangee: "+nourriture.toString());
                        for(PointSerialisable nour : food) {
                            if(nour.equals(nourriture)) {
                                nourriture=nour;
                                break;
                            }
                        }
                        food.remove(nourriture);
                        data.setFoodEaten(null);
                    }
                    System.out.println(data.toString());
                    PointSerialisable posSnake = data.getPos();
                    PointSerialisable posPtr = data.getPointeur();
                    Point2D milieu = new Point2D(640, 360);
                    Point2D distance = new Point2D(posPtr.getX()-milieu.getX(), posPtr.getY()-milieu.getY());
                    Point2D newPosPtr = new Point2D(posSnake.getX()+distance.getX(), posSnake.getY()+distance.getY());
                    // Point2D newPtrPos = new Point2D(data.getP);
                    data.setPointeur(new PointSerialisable(newPosPtr));
                    //System.out.println(data.getPointeur().getX()+" "+ data.getPointeur().getY());
                    //envoi des coord a tous les joueurs

                    //generation d'une nouvelle nourriture
                    if (clients.size()>=2) {
                        boolean b = genereNourriture();
                        if (b) data.setNewFood(food.getLast());
                    }  

                    sendMsgToAll(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //cas ou le joueur appelle disconnect() ou quitte le programme
                try {
                    //envoi cadavre
                    
                    System.out.println("termine");
                    sendMsgToAll(new SlitherData(data.getStartTime(), new Point2D(-1,-1), new Point2D(-1, -1), 0));
                    in.close();
                    out.close();
                    clientSocket.close();
                    clients.remove(this);
                } catch (Exception e) {
                    System.out.println("Erreur client : " + e.getMessage());
                }
            }
        }

        public SlitherData getData() {
            return data;
        }

        public long getId() {
            return id;
        }

        public Socket getClientSocket() {
            return clientSocket;
        }
    }


    private Object verrou = new Object();

    public void sendMsgToAll(SlitherData data) {
        synchronized (verrou) {
            for (ClientHandler c : clients) {
                try {
                    //envoi de la nourriture de base
                    if (!c.baseFoodSent) {
                        data.setFoodBase(food);
                        c.baseFoodSent=true;
                    }
                    c.out.writeObject(data);
                    data.setFoodBase(new LinkedList<>());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean genereNourriture() {
        if (food.size()<1000) {
            Random rand = new Random();
            int nb = rand.nextInt(30*clients.size());
            if (nb==0) {
                PointSerialisable ns = new PointSerialisable(new Point2D(rand.nextDouble(1280-30), rand.nextDouble(720-30)));
                food.addLast(ns);
                return true;
            }
        }
        return false;
    }

    /*public void sendId(ClientHandler receiver) {
        try {
            PrintWriter pw = new PrintWriter(new ObjectOutputStream(receiver.getClientSocket().getOutputStream()));
            pw.print(""+receiver.getId());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

}