package fr.redspri.tanmiachat.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor@Data
public class ServerSettings {
    String host;
    Integer port;
    String pass;
}
