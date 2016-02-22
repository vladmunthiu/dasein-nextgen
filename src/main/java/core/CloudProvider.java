package core;

import azure.AzureComputeServiceModule;
import azurepack.AzurePackComputeServiceModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by vmunthiu on 10/12/2015.
 */
public class CloudProvider {
    private String endpoint;
    private String accountNumber;
    private String providerName;
    private String regionId;
    private String sharedKey;
    private String secreteKey;
    private HttpClientBuilder httpClientBuilder;

    public CloudProvider(String endpoint, String accountNumber, String providerName, String regionId, String sharedKey, String secreteKey) {
        this.endpoint = endpoint;
        this.accountNumber = accountNumber;
        this.providerName = providerName;
        this.regionId = regionId;
        this.sharedKey = sharedKey;
        this.secreteKey = secreteKey;
    }

    public ComputeService getComputeService() {
        AbstractModule computeServiceModule = null;
        //if cloud drivers are in different projects we need to create the module instance via reflection
        //for proof of concept manual instance creation is enough
        if(providerName.equalsIgnoreCase("azure")) {
            computeServiceModule = new AzureComputeServiceModule(this);
        } else if( providerName.equalsIgnoreCase("azurepack")) {
            computeServiceModule = new AzurePackComputeServiceModule(this);
        }

        Injector injector = Guice.createInjector(computeServiceModule);
        return injector.getInstance(ComputeService.class);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public HttpClientBuilder getHttpClientBuilder() {
        return getAzureClientBuilder();
    }

    private HttpClientBuilder getAzureClientBuilder()  {
        try {
            boolean disableSSLValidation = true;
            HttpClientBuilder builder = HttpClientBuilder.create();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", new AzureSSLSocketFactory(new AzureX509(this), disableSSLValidation))
                    .build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(20);
            builder.setConnectionManager(connManager);
            return builder;
        } catch (Exception e) {
            return null;
        }
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public String getSecreteKey() {
        return secreteKey;
    }

    public String getRegionId() {
        return regionId;
    }


    //for proof of concept azure classes can stay here
    public static class AzureSSLSocketFactory extends SSLSocketFactory {

        public AzureSSLSocketFactory(AzureX509 creds, boolean disableSSLValidation) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super("TLS", creds.getKeystore(), AzureX509.PASSWORD, null, null, disableSSLValidation ? trustStrategy : null, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }

        private static final TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };
    }

    public static class AzureX509 {
        static public final String ENTRY_ALIAS = "";
        static public final String PASSWORD    = "memory";

        static {
            Security.addProvider(new BouncyCastleProvider());
        }

        private KeyStore keystore;

        public AzureX509(CloudProvider provider) throws Exception {
            //ProviderContext ctx = provider.getContext();
            String apiShared = provider.getSharedKey();
            String apiSecret = provider.getSecreteKey();
//                try {
//                    List<ContextRequirements.Field> fields = provider.getContextRequirements().getConfigurableValues();
//                    for(ContextRequirements.Field f : fields ) {
//                        if(f.type.equals(ContextRequirements.FieldType.KEYPAIR)){
//                            byte[][] keyPair = (byte[][])ctx.getConfigurationValue(f);
//                            apiShared = new String(keyPair[0], "utf-8");
//                            apiSecret = new String(keyPair[1], "utf-8");
//                        }
//                    }
            //  System.out.println(apiShared);
                //  System.out.println(apiSecret);

                X509Certificate certificate = certFromString(apiShared);
                PrivateKey privateKey = keyFromString(apiSecret);

                keystore = createJavaKeystore(certificate, privateKey);

        }

        private static X509Certificate certFromString(String pem) throws Exception {
            PemObject pemObject = (PemObject) readPemObject(pem);
            ByteArrayInputStream inputStream= new ByteArrayInputStream(pemObject.getContent());
            try {
                CertificateFactory certFact = CertificateFactory.getInstance("X.509");
                return (X509Certificate) certFact.generateCertificate(inputStream);
            } catch (CertificateException e) {
                throw new Exception("problem parsing cert: " + e.toString(),e);
            }
        }

        private KeyStore createJavaKeystore(X509Certificate cert, PrivateKey key) throws NoSuchProviderException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
            KeyStore store = KeyStore.getInstance("JKS", "SUN");
            char[] pw = PASSWORD.toCharArray();

            store.load(null, pw);
            store.setKeyEntry(ENTRY_ALIAS, key, pw, new java.security.cert.Certificate[] {cert});
            return store;
        }
        public KeyStore getKeystore() {
            return keystore;
        }

        private PrivateKey keyFromString(String pem) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
            PemObject pemObject = (PemObject) readPemObject(pem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }

        private static Object readPemObject(String pemString) throws IOException {
            StringReader strReader = new StringReader(pemString);
            PemReader pemReader = new PemReader(strReader);

            try {
                return pemReader.readPemObject();
            }
            finally {
                strReader.close();
                pemReader.close();
            }
        }
    }
}
