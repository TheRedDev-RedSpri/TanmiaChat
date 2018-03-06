package fr.redspri.tanmiachat.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor@Data
public class ClientSettings {
    String name;
    ServerSettings settings;
}
