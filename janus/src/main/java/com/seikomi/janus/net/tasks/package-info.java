/**
 * This package contains all the task associated with a Janus server. This tasks
 * run on a respective thread. All inherits of {@link JanusTask}. The
 * ConnectTask dispatch the client and assigned him to a
 * TreatmentTask that execute the commands receive. The
 * DownloadTask send files to the client.
 */
package com.seikomi.janus.net.tasks;