#Network Simulator Project

#importing modules
import numpy as np

#Importing Data

Good_data = open(r"C://Users//abhis//Desktop//FSU//Data Comm//Project3//DATA_GOOD_2", "r")
data = np.loadtxt(Good_data, dtype="int")
Good_ack = open(r"C://Users//abhis//Desktop//FSU//Data Comm//Project3//ACK_GOOD_2", "r")
ack = np.loadtxt(Good_ack, dtype="int")
f = open("C://Users//abhis//Desktop//FSU//Data Comm//Project3//Usha_Go_back_N.txt", "w")

#Initializing Variables
start = 0
retransmission = 0
end = start + 6
current_time = 0
trans_frame = 0
retrans_frame = 0
current_time = 0
receiver_ack = 0
receiver_ack_list = []
trans_ack_list = []
trans_ack_time = []
trans_ack = 0
receiver_frame_list = []
receiver_frame_time = []
counter = 0
expected_ack = []
a = 0
j = 0
x = 0
timeout = -1
timeout = []
bad_ACK=[]
good_frames = []
retrans_f=[]
good = []
bad_data = []

#Receiver Function
def ReceiverFunction():
    global receiver_frame_time
    global receiver_frame_list
    global j
    global x
    global bad_data
    global trans_ack
    global trans_ack_list
    global trans_ack_time
    global counter
    if good != [] and counter < len(good):
        if receiver_frame_time != []:
            if receiver_frame_time[0] == current_time:
                receiver_frame_list.append(good_frames[counter])
                receiver_frame_time.pop(0)
                if bad_data != []:
                    if good_frames[counter] in bad_data:
                        bad_data.remove(good_frames[counter])
        if current_time - good[counter] == 4:
                if trans_ack in receiver_frame_list and trans_ack not in bad_data:
                    trans_ack += 1
                if ack[j] == 1:
                    print("time {}: receiver got frame {}, transmitting ACK{}, good transmission ".format(current_time,good_frames[counter],trans_ack), file=f)
                    trans_ack_list.append(trans_ack)
                    trans_ack_time.append(current_time)
                if ack[j] == 0:
                    print("time {}: receiver got frame {}, transmitting ACK{}, badtransmission ".format(current_time,good_frames[counter],trans_ack), file=f)
                    bad_ACK.append(trans_ack)
                j += 1
                counter += 1
                
     #Sender Function           
def SenderFunction():
        global receiver_ack
        global receiver_ack_list
        global x
        global start
        global end
        global retrans_frame
        if current_time == 0:
            print("time {}: sender got ACK{}, window [{}, {}]".format(current_time, 0,start, end), file=f)
        else:
            if trans_ack_time != [] and x < len(trans_ack_time):
                if current_time - trans_ack_time[x] == 3:
                    receiver_ack = trans_ack_list[x]
                    start = receiver_ack
                    end = start + 6
                    print("time {}: sender got ACK{}, window [{},{}]".format(current_time, receiver_ack, start, end), file=f)
                    receiver_ack_list.append(receiver_ack)
                    if retrans_frame not in range(start, end + 1):
                            rt = 0
                            x += 1
def TransmissionFunction():
    global a
    global good_frames
    global good
    global bad_data
    global trans_frame
    global retrans_frame
    global timeout
    global retransmission
    global data
    global expected_ack
    global bad_ACK
    if timeout != []:
         if timeout[0] == current_time:
            if expected_ack[0] not in receiver_ack_list:
                if expected_ack[0] > receiver_ack_list[-1]:
                    retransmission=1
                    retrans_frame = expected_ack[0] - 1
                    timeout.pop(0)
                    expected_ack.pop(0)
    if retransmission == 0 :
        if trans_frame in list(range(start, end + 1)):
                if data[a] == 1:
                    print("time {}: sender window [{}, {}], transmitting new frame {},good transmission ".format(current_time,start, end,trans_frame), file=f)
                    good_frames.append(trans_frame)
                    good.append(current_time)
                    receiver_frame_time.append(current_time + 4)
                if data[a] == 0:
                    print("time {}: sender window [{}, {}], transmitting new frame {} bad transmission ".format(current_time,start, end,trans_frame), file=f)
                    bad_data.append(trans_frame)
                    trans_frame += 1
                    expected_ack.append(trans_frame)
                    timeout.append(current_time + 7 + 1)
                    a += 1
                if retransmission == 1:
                    if retrans_frame in range(start, end + 1):
                        if data[a] == 1:
                            print("time {}: sender window [{}, {}], retransmitting old frame {}, good transmission ".format(current_time,start, end,retrans_frame), file=f)
                            good.append(current_time)
                            receiver_frame_time.append(current_time + 4)
                            good_frames.append(retrans_frame)
                        if data[a] == 0:
                            print("time {}: sender window [{}, {}], retransmitting old frame{}, bad transmission ".format(current_time,start, end, retrans_frame), file=f)
                        if retrans_frame not in bad_data:
                            bad_data.append(retrans_frame)
                            retrans_frame += 1
                            expected_ack.append(retrans_frame)
                            retrans_frame += 1
                            timeout.append(current_time + 7 + 1)
                            a += 1


while True:
    ReceiverFunction()
    SenderFunction()
    TransmissionFunction()
    retransmission=0
    current_time += 1