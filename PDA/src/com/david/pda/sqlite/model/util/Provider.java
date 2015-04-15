package com.JAC.stubank.provider;

import com.JAC.stubank.provider.DatabaseConstant.AccountConstant;
import com.JAC.stubank.provider.DatabaseConstant.AddressConstant;
import com.JAC.stubank.provider.DatabaseConstant.BankcardConstant;
import com.JAC.stubank.provider.DatabaseConstant.TradeConstant;
import com.JAC.stubank.provider.DatabaseConstant.TotradeConstant;
import com.JAC.stubank.provider.DatabaseConstant.LoanrequestConstant;
import com.JAC.stubank.provider.DatabaseConstant.StumsgConstant;
import com.david.pda.sqlite.model.Alarm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class Provider extends ContentProvider {
	private DatabaseHelper dbHelper;
	private static final int ACCOUNT_DATA = 1;
	private static final int ADDRESS_DATA = 2;
	private static final int BANKCARD_DATA = 3;
	private static final int TRADE_DATA = 4;
	private static final int TOTRADE_DATA = 5;
	private static final int LOANREQUEST_DATA = 6;
	private static final int STUMSG_DATA = 7;
	private static final UriMatcher URI_MATHER;
	static {
		URI_MATHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATHER.addURI(DatabaseConstant.AUTHORITY, Alarm.TABLE_NAME, ACCOUNT_DATA);
		
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DatabaseHelper(getContext());
		return false;
	}
	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		String having = null;
		switch (URI_MATHER.match(uri)) {
		case ACCOUNT_DATA:
			qb.setTables(AccountConstant.TABLE_NAME);
			break;
		case ADDRESS_DATA:
			qb.setTables(AddressConstant.TABLE_NAME);
			break;
		case BANKCARD_DATA:
			qb.setTables(BankcardConstant.TABLE_NAME);
			break;
		case TRADE_DATA:
			qb.setTables(TradeConstant.TABLE_NAME);
			break;
		case TOTRADE_DATA:
			qb.setTables(TotradeConstant.TABLE_NAME);
			break;
		case LOANREQUEST_DATA:
			qb.setTables(LoanrequestConstant.TABLE_NAME);
			break;
		case STUMSG_DATA:
			qb.setTables(StumsgConstant.TABLE_NAME);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri);
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, groupBy,
				having, sortOrder);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rId = -1;
		switch (URI_MATHER.match(uri)) {
		case ACCOUNT_DATA:
			rId = db.insert(AccountConstant.TABLE_NAME, null, values);
			break;
		case ADDRESS_DATA:
			rId = db.insert(AddressConstant.TABLE_NAME, null, values);
			break;
		case BANKCARD_DATA:
			rId = db.insert(BankcardConstant.TABLE_NAME, null, values);
			break;
		case TRADE_DATA:
			rId = db.insert(TradeConstant.TABLE_NAME, null, values);
			break;
		case TOTRADE_DATA:
			rId = db.insert(TotradeConstant.TABLE_NAME, null, values);
			break;
		case LOANREQUEST_DATA:
			rId = db.insert(LoanrequestConstant.TABLE_NAME, null, values);
			break;
		case STUMSG_DATA:
			rId = db.insert(StumsgConstant.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri);
		}
		return ContentUris.withAppendedId(uri, rId);
	}
// when use contentResolver, first we try update, if failed, insert
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = -1;
		switch (URI_MATHER.match(uri)) {
		case ACCOUNT_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case ADDRESS_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case BANKCARD_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case TRADE_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case TOTRADE_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case LOANREQUEST_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		case STUMSG_DATA:
			count = updateOrInsert(db, uri, values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri);
		}
		return count;
	}

	private int updateOrInsert(SQLiteDatabase db, Uri uri,
			ContentValues values, String selection, String[] selectionArgs) {
		int count;
		switch (URI_MATHER.match(uri)) {
		case ACCOUNT_DATA:
			count = db.update(AccountConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case ADDRESS_DATA:
			count = db.update(AddressConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case BANKCARD_DATA:
			count = db.update(BankcardConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case TRADE_DATA:
			count = db.update(TradeConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case TOTRADE_DATA:
			count = db.update(TotradeConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case LOANREQUEST_DATA:
			count = db.update(LoanrequestConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		case STUMSG_DATA:
			count = db.update(StumsgConstant.TABLE_NAME, values, selection,
					selectionArgs);
			if (count == 0) {
				count = Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri);
		}
		return count;
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = 0;
		switch (URI_MATHER.match(uri)) {
		case ACCOUNT_DATA:
			count = db.delete(AccountConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case ADDRESS_DATA:
			count = db.delete(AddressConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case BANKCARD_DATA:
			count = db.delete(BankcardConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case TRADE_DATA:
			count = db.delete(TradeConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case TOTRADE_DATA:
			count = db.delete(TotradeConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case LOANREQUEST_DATA:
			count = db.delete(LoanrequestConstant.TABLE_NAME, selection, selectionArgs);
			break;
		case STUMSG_DATA:
			count = db.delete(StumsgConstant.TABLE_NAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri);
		}
		return count;
	}


}
