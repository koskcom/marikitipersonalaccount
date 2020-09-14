package bsl.co.ke.fundsmanagementapi.newtwork.licensesdialog.licenses;

import android.content.Context;

import bsl.co.ke.fundsmanagementapi.R;


public class EclipsePublicLicense10 extends License {

    @Override
    public String getName() {
        return "Eclipse Public License 1.0";
    }

    @Override
    public String readSummaryTextFromResources(Context context) {
        return getContent(context, R.raw.epl_v10_summary);
    }

    @Override
    public String readFullTextFromResources(Context context) {
        return getContent(context, R.raw.epl_v10_full);
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getUrl() {
        return "https://www.eclipse.org/legal/epl-v10.html";
    }
}
