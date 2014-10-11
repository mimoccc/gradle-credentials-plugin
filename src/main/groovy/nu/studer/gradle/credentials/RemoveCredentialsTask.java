package nu.studer.gradle.credentials;

import nu.studer.gradle.credentials.domain.CredentialsPersistenceManager;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Removes the given credentials, specified as project properties.
 */
public class RemoveCredentialsTask extends DefaultTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveCredentialsTask.class);

    private CredentialsPersistenceManager credentialsPersistenceManager;

    public void setCredentialsPersistenceManager(CredentialsPersistenceManager credentialsPersistenceManager) {
        this.credentialsPersistenceManager = credentialsPersistenceManager;
    }

    @Input
    public String getCredentialsKey() {
        return getProjectProperty(CredentialsPlugin.CREDENTIALS_KEY_PROPERTY);
    }

    @OutputFile
    public File getEncryptedPropertiesFile() {
        return credentialsPersistenceManager.getCredentialsFile();
    }

    @TaskAction
    void removeCredentials() throws IOException {
        // get credentials key from the project properties
        String key = getCredentialsKey();

        LOGGER.debug(String.format("Remove credentials with key: '%s'", key));

        // read the current persisted credentials
        Properties credentials = credentialsPersistenceManager.readCredentials();

        // remove the credentials with the given key
        credentials.remove(key);

        // persist the updated credentials
        credentialsPersistenceManager.storeCredentials(credentials);
    }

    private String getProjectProperty(String key) {
        return (String) getProject().getProperties().get(key);
    }

}