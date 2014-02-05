package ru.ifmo.baev.chat.gui;

import ru.ifmo.baev.chat.data.ChatData;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public final class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageArea;
    private GridBagConstraints constraints;
    private ChatData chatData;

    private ChatGUI(ChatData chatData) throws HeadlessException {
        this.chatData = chatData;
        chatArea = new JTextArea(30, 50);
        messageArea = new JTextField(30);
        constraints = new GridBagConstraints();
    }

    private void setLayout() {
        getContentPane().setLayout(new GridBagLayout());
    }

    private void addChatLabel() {
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel chatLabel = new JLabel("Chat");
        getContentPane().add(chatLabel, constraints);
    }

    private void addMessageLabel() {
        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel messageLabel = new JLabel("Message");
        getContentPane().add(messageLabel, constraints);
    }

    private void addScrollPane() {
        constraints.gridx = 0;
        constraints.gridy = 1;
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        getContentPane().add(chatScrollPane, constraints);
    }

    private void addMessageArea() {
        constraints.gridx = 0;
        constraints.gridy = 3;
        getContentPane().add(messageArea, constraints);
    }

    private void addSubmitButton() {
        constraints.gridx = 0;
        constraints.gridy = 4;
        JButton submitButton = new JButton("Send");
        submitButton.addActionListener(new SendActionListener(chatData, this));
        getContentPane().add(submitButton, constraints);
        getRootPane().setDefaultButton(submitButton);
    }

    public static ChatGUI createChatGUI(ChatData chatData) {
        final ChatGUI gui = new ChatGUI(chatData);
        gui.setLayout();
        gui.addChatLabel();
        gui.addScrollPane();
        gui.addMessageArea();
        gui.addMessageLabel();
        gui.addSubmitButton();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                gui.setSize(450, 600);
                gui.pack();
                gui.setVisible(true);
            }
        });

        return gui;
    }

    public void updateChatArea(String text) {
        chatArea.append(text);
    }

    public JTextArea getChatArea() {
        return chatArea;
    }

    public JTextField getMessageArea() {
        return messageArea;
    }
}