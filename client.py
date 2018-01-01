import curses, socket, threading
from curses import wrapper



class ChatBox:

    address = "localhost"
    port = 8181
    text = ""
    chat_history = []
    refresh_components = []
    data_buffer = []

    def __init__(self,screen,height,width,y,x):
        self.screen = screen
        curses.echo()

        # dimensions
        self.y = y
        self.x = x
        self.height = height
        self.width = width
        self.chat_y = self.y
        self.chat_x = self.x
        self.send_y = self.y + 30
        self.send_x = self.x
        self.send_height = self.height-25

        # create sending and receiving window
        self.chat_window = curses.newwin(self.height,self.width,self.chat_y,self.chat_x)
        self.send_window = curses.newwin(self.send_height,self.width,self.send_y,self.send_x)

        # box the windows
        self.send_window.box()
        self.chat_window.box()

        # add each component to refresh list
        self.refresh_components.append(self.screen)
        self.refresh_components.append(self.chat_window)
        self.refresh_components.append(self.send_window)

        # connect to socket and spawn read thread
        self.connect(self.address,self.port)
        t1 = threading.Thread(target=self.receive_data)
        t1.daemon = True
        t1.start()

    def do_refresh(self):
        self.send_window.clear()
        self.chat_window.clear()
        for component in self.refresh_components:
            self.chat_window.box()
            self.send_window.box()
            component.refresh()
    def send_to_network(self):
        self.send_window.addstr(1,1,"Enter message: ")
        self.send_window.refresh()
        self.text = self.send_window.getstr()
        self.s.send(self.text+"\n")
        self.screen.addstr(15,15,self.text)
        self.data_buffer.append(self.text)

    def connect(self,address,port):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((address,port))

    def write_chat_history(self):
        self.do_refresh()
        self.count = 0
        for line in self.data_buffer:
            self.chat_window.addstr(1+self.count,1,line)
            self.screen.refresh()
            self.chat_window.refresh()
            self.chat_window.box()
            self.count = self.count + 1

    def receive_data(self):
        while True:
            res = self.s.recv(1024)
            if res != '':
                self.data_buffer.append(res)
                self.write_chat_history()



def test(screen):
    box = ChatBox(screen,30,80,0,0)
    while True:
        box.write_chat_history()
        box.send_to_network()



wrapper(test)
