
package com.battlelancer.seriesguide.billing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.battlelancer.seriesguide.ui.BaseActivity;
import com.uwetrottmann.seriesguide.R;

public class BillingActivity extends BaseActivity {

    public static final String TAG = "BillingActivity";

    // The SKU product id as set in the Developer Console
    private static final String SKU_X = "x_upgrade";

    private IabHelper mHelper;

    // If the user already has the X upgrade
    private boolean mHasXUpgrade = false;

    private Button mUpgradeButton;

    private View mTextHasUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing);

        mUpgradeButton = (Button) findViewById(R.id.buttonBillingGetUpgrade);
        mTextHasUpgrade = findViewById(R.id.textViewBillingExisting);

        updateUi();

        String base64EncodedPublicKey = null;
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    Toast.makeText(getApplicationContext(),
                            "Problem setting up In-app Billing: " + result,
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Hooray, IAB is fully set up. Now, let's get an inventory of
                // stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    private void updateUi() {
        // Only enable purchase button if the user does not have the upgrade yet
        mUpgradeButton.setEnabled(!mHasXUpgrade);
        mTextHasUpgrade.setVisibility(mHasXUpgrade ? View.VISIBLE : View.GONE);
    }

    // Listener that's called when we finish querying the items and
    // subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                // TODO handle error
                Toast.makeText(getApplicationContext(), "Could not query inventory: " + result,
                        Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_X);
            mHasXUpgrade = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User has " + (mHasXUpgrade ? "X UPGRADE" : "NOT X UPGRADE"));

            updateUi();
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    /** Verifies the developer payload of a purchase. */
    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct.
         * It will be the same one that you sent when initiating the purchase.
         * WARNING: Locally generating a random string when starting a purchase
         * and verifying it here might seem like a good approach, but this will
         * fail in the case where the user purchases an item on one device and
         * then uses your app on a different device, because on the other device
         * you will not have access to the random string you originally
         * generated. So a good developer payload has these characteristics: 1.
         * If two different users purchase an item, the payload is different
         * between them, so that one user's purchase can't be replayed to
         * another user. 2. The payload must be such that you can verify it even
         * when the app wasn't the one who initiated the purchase flow (so that
         * items purchased by the user on one device work on other devices owned
         * by the user). Using your own server to store and verify developer
         * payloads across app installations is recommended.
         */

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
    }

}
