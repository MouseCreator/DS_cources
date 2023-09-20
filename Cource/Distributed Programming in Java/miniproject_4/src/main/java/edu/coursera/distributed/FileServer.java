package edu.coursera.distributed;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class FileServer {
    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs A proxy filesystem to serve files from. See the PCDPFilesystem
     *           class for more detailed documentation of its usage.
     * @param ncores The number of cores that are available to your
     *               multi-threaded file server. Using this argument is entirely
     *               optional. You are free to use this information to change
     *               how you create your threads, or ignore it.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */

    public void run(final ServerSocket socket, final PCDPFilesystem fs,
            final int ncores) throws IOException {
        /*
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        ThreadPool threadPool = new ThreadPool(ncores, fs);
        while (true) {


            // TODO 1) Use socket.accept to get a Socket object
            Socket s = socket.accept();
            threadPool.execute(new ExecutiveTask(s));
            /*
             * TODO 2) Now that we have a new Socket object, handle the parsing
             * of the HTTP message on that socket and returning of the requested
             * file in a separate thread. You are free to choose how that new
             * thread is created. Common approaches would include spawning a new
             * Java Thread or using a Java Thread Pool. The steps to complete
             * the handling of HTTP messages are the same as in MiniProject 2,
             * but are repeated below for convenience:
             *
             *   a) Using Socket.getInputStream(), parse the received HTTP
             *      packet. In particular, we are interested in confirming this
             *      message is a GET and parsing out the path to the file we are
             *      GETing. Recall that for GET HTTP packets, the first line of
             *      the received packet will look something like:
             *
             *          GET /path/to/file HTTP/1.1
             *   b) Using the parsed path to the target file, construct an
             *      HTTP reply and write it to Socket.getOutputStream(). If the
             *      file exists, the HTTP reply should be formatted as follows:
             *
             *        HTTP/1.0 200 OK\r\n
             *        Server: FileServer\r\n
             *        \r\n
             *        FILE CONTENTS HERE\r\n
             *
             *      If the specified file does not exist, you should return a
             *      reply with an error code 404 Not Found. This reply should be
             *      formatted as:
             *
             *        HTTP/1.0 404 Not Found\r\n
             *        Server: FileServer\r\n
             *        \r\n
             *
             * If you wish to do so, you are free to re-use code from
             * MiniProject 2 to help with completing this MiniProject.
             */
        }
    }
}

class ExecutiveTask {
    private Socket socket;
    public ExecutiveTask(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
class ThreadPool {
    private final int nThreads;
    private final Thread[] threads;
    private final BlockingQueue<ExecutiveTask> queue;

    public ThreadPool(int nThreads, PCDPFilesystem fs) {
        this.nThreads = nThreads;
        this.queue = new LinkedBlockingQueue<>();
        this.threads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(new ThreadRunnable(fs));
            threads[i].start();
        }
    }

    public void execute(ExecutiveTask task) {
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    class ThreadRunnable implements Runnable {
        private final PCDPFilesystem filesystem;

        ThreadRunnable(PCDPFilesystem filesystem) {
            this.filesystem = filesystem;
        }

        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                try {
                    ExecutiveTask task = queue.take();
                    processSocket(task.getSocket());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processSocket(Socket socket) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                /*
                 * TODO 2) Using Socket.getInputStream(), parse the received HTTP
                 * packet. In particular, we are interested in confirming this
                 * message is a GET and parsing out the path to the file we are
                 * GETing. Recall that for GET HTTP packets, the first line of the
                 * received packet will look something like:
                 *
                 *     GET /path/to/file HTTP/1.1
                 */

                String line;
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    if (line.trim().startsWith("GET")) {
                        break;
                    }
                }
                if (line == null) {
                    throw new RuntimeException("Could not parse HTTP request");
                }
                /*
                 * TODO 3) Using the parsed path to the target file, construct an
                 * HTTP reply and write it to Socket.getOutputStream(). If the file
                 * exists, the HTTP reply should be formatted as follows:
                 *
                 *   HTTP/1.0 200 OK\r\n
                 *   Server: FileServer\r\n
                 *   \r\n
                 *   FILE CONTENTS HERE\r\n
                 *
                 * If the specified file does not exist, you should return a reply
                 * with an error code 404 Not Found. This reply should be formatted
                 * as:
                 *
                 *   HTTP/1.0 404 Not Found\r\n
                 *   Server: FileServer\r\n
                 *   \r\n
                 *
                 * Don't forget to close the output stream.
                 */
                line = parseLine(line);

                PCDPPath pathToFile = new PCDPPath(line);
                String content = filesystem.readFile(pathToFile);
                String httpReply;
                if (content != null) {
                    httpReply = "HTTP/1.0 200 OK\r\nServer: FileServer\r\n\r\n" + content + "\r\n";
                } else {
                    httpReply = "HTTP/1.0 404 Not Found\r\nServer: FileServer\r\n\r\n";
                }
                writer.write(httpReply);
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private String parseLine(String line) {
            String output = line.trim();
            int start = output.indexOf("GET") + 4;
            int end = output.indexOf("HTTP") - 1;
            return output.substring(start, end);
        }
    }
}
