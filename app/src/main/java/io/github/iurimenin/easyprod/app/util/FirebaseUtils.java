package io.github.iurimenin.easyprod.app.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class FirebaseUtils {
    
    private final String FARM_REFERENCE = "/farms/";
    private final String FIELD_REFERENCE = "/fields/";
    private final String SEASON_REFERENCE = "/seasons/";
    private final String PRODUCTION_REFERENCE = "/productions/";
    private FirebaseUser mUser;
    private static FirebaseDatabase mFirebaseDatabase;

    public FirebaseUtils() {
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirebaseDatabase getFirebaseDatabase() {

        if (mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase.setPersistenceEnabled(true);
        }
        return mFirebaseDatabase;
    }
    
    public final DatabaseReference getFarmReference() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getEmailWithouDots());
        sb.append(this.FARM_REFERENCE);
        DatabaseReference reference = getFirebaseDatabase().getReference(sb.toString());
        reference.keepSynced(true);
        return reference;
    }

    
    public final DatabaseReference getFieldReference( String farmKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getEmailWithouDots());
        sb.append(this.FIELD_REFERENCE);
        sb.append("farmKey(");
        sb.append(farmKey);
        sb.append(")");
        DatabaseReference reference = getFirebaseDatabase().getReference(sb.toString());
        reference.keepSynced(true);
        return reference;
    }

    
    public final DatabaseReference getSeasonReference() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.SEASON_REFERENCE);
        DatabaseReference reference = getFirebaseDatabase().getReference(sb.toString());
        reference.keepSynced(true);
        return reference;
    }

    
    public final DatabaseReference getProductionReference( String mCultivationKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getEmailWithouDots());
        sb.append(this.PRODUCTION_REFERENCE);
        sb.append("cultivationKey(");
        sb.append(mCultivationKey);
        sb.append(")");
        DatabaseReference reference = getFirebaseDatabase().getReference(sb.toString());
        reference.keepSynced(true);
        return reference;
    }

    private final String getEmailWithouDots() {
        return mUser.getEmail().replace(".", "|");
    }
}
