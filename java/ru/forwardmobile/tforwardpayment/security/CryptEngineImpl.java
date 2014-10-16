package ru.forwardmobile.tforwardpayment.security;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

import java.math.BigInteger;

import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.md5;


/**
 *
 * @author vaninv
 */
public class CryptEngineImpl implements ICryptEngine {
    
    private final MD5Digest                     digest;
    private final AsymmetricBlockCipher         cipher;
    private final RSAKeyParameters              publicRsaKey;
    private final RSAPrivateCrtKeyParameters    privateRsaKey;
    
    public CryptEngineImpl(Context ctx) throws Exception {
        
        // Получаем действующее хранилище
        IKeyStorage storage = KeyStorageFactory.getKeyStorage(ctx);
       
        Log.v("TFORWARD.CryptEngineImpl", "Decoding public key...");
        byte[] publicKey = Base64.decode( storage.getKey(IKeyStorage.PUBLIC_KEY_TYPE), Base64.DEFAULT);
            
        Log.v("TFORWARD.CryptEngineImpl", "Decoding ASN1 Structure");
        ASN1InputStream asnStream = new ASN1InputStream(publicKey);
        
        
        ASN1Sequence sequence = null;
        try {
            Log.v("TFORWARD.CryptEngineImpl", "Reading ASN1 Sequence");
            sequence = (ASN1Sequence)  asnStream.readObject();
        } finally {
            asnStream.close();
        }
        
        Log.v("TFORWARD.CryptEngineImpl", "Creating certificate. " + sequence.size());
        Certificate certificate = Certificate.getInstance(sequence);
        SubjectPublicKeyInfo publicKeyInfo = certificate.getSubjectPublicKeyInfo();
        
        RSAPublicKey publicKeyStructure = RSAPublicKey.getInstance(publicKeyInfo.parsePublicKey());
        BigInteger mod = publicKeyStructure.getModulus();
        BigInteger pubExp = publicKeyStructure.getPublicExponent();
           
        publicRsaKey = new RSAKeyParameters(false, mod, pubExp);    

        // ------------------------ PRIVATE KEY --------------------------------
        byte[] privateKeyData = Base64.decode( storage.getKey(IKeyStorage.SECRET_KEY_TYPE) , Base64.DEFAULT);
        asnStream = new ASN1InputStream(privateKeyData);
        
        ASN1Sequence asnSequence = null;
        try {
            asnSequence = (ASN1Sequence) asnStream.readObject();
        } finally {
            asnStream.close();
        }        

        RSAPrivateKey privateKey = RSAPrivateKey.getInstance(asnSequence);
        privateRsaKey = new RSAPrivateCrtKeyParameters(
                            privateKey.getModulus(),
                            privateKey.getPublicExponent(),
                            privateKey.getPrivateExponent(),
                            privateKey.getPrime1(), 
                            privateKey.getPrime2(),
                            privateKey.getExponent1(), 
                            privateKey.getExponent2(),
                            privateKey.getCoefficient()
                        );
        
        RSAEngine engine = new RSAEngine();
        digest = new MD5Digest();
        cipher = new PKCS1Encoding(engine);        
    }
    
    
    
    public synchronized String generateSignature(byte[] message) throws Exception {
        // Log.trace("Signed message length is " + message.length);
        
        try {
            digest.reset();
            cipher.init(true, privateRsaKey);
            digest.update(message, 0, message.length);
            byte[] hash = new byte[digest.getDigestSize()];
            digest.doFinal(hash, 0);
            DigestInfo info = new DigestInfo( new AlgorithmIdentifier(md5, null), hash);
            byte[] bytes = info.getEncoded(ASN1Encoding.DER);
            byte[] signature = cipher.processBlock(bytes, 0, bytes.length);
            return new String(org.bouncycastle.util.encoders.Base64.encode(signature));
        } catch(Exception e) {
            throw new Exception("Ошибка создания подписи:\n" + e.getMessage());
        }
    }

    public synchronized String generateSignature(String source) throws Exception {
        return generateSignature(source.getBytes());
    }

    public synchronized void verifySignature(byte[] message, byte[] signature) throws Exception {
        try {
            digest.reset();
            cipher.init(false, publicRsaKey);
            digest.update(message, 0, message.length);
            byte[] hash = new byte[digest.getDigestSize()];
            digest.doFinal(hash, 0);
            DigestInfo info = new DigestInfo(new AlgorithmIdentifier(md5, null), hash);
            byte[] bytes = info.getEncoded(ASN1Encoding.DER);
            byte[] signatureData = org.bouncycastle.util.encoders.Base64.decode(signature);
            byte[] result = cipher.processBlock(signatureData, 0, signatureData.length);
            if ( ( result == null ) || ( result.length < hash.length ) ) {
                throw new Exception("Invalid signature (1)!");
            }
            if ( !compareFromTheEnd(hash, result) ) {
                throw new Exception("Invalid signature (2)!");
            }
        } catch(Exception e) {
            throw new Exception("Error checking signature:\n" + e.getMessage());
        }
    }

    private boolean compareFromTheEnd(byte[] a, byte[] b) {
        int i = a.length - 1;
        int j = b.length - 1;
        if (i == -1)
            return (j == -1);
        if (j == -1)
            return (i == -1);
        while (i != -1 && j != -1) {
            if (a[i--] != b[j--])
                return false;
        }
        return true;
    }    
    
}
