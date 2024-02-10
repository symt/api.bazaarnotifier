package dev.meyi.bn.response.impl.admin;

import dev.meyi.bn.response.Response;

/**
 * Response object for /admin/db_dump/{player|auth}
 *
 * @param <T> Player or Auth depending on the table (but can be expanded for others
 */
public class DatabaseDumpResponse<T> extends Response<T> {

  public DatabaseDumpResponse(T dbEntries) {
    this.success = true;
    this.response = dbEntries;
  }
}
