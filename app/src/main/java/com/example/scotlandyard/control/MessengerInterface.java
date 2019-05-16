package com.example.scotlandyard.control;

import com.example.scotlandyard.messenger.Message;

import java.util.ArrayList;

public interface MessengerInterface {
    void updateMessages(ArrayList<Message> messages);
}
