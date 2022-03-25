package com.juntai.wisdom.im.base.uploadFile;


import com.juntai.disabled.basecomponent.utils.LogUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;


/**
 * 扩展OkHttp的请求体，实现上传时的进度提示
 */
public class UploadFileRequestBody<T> extends RequestBody {


    private RequestBody mRequestBody;
    private FileUploadObserver<UploadFileBean> fileUploadObserver;
    private UploadFileBean uploadFileBean;

    public UploadFileRequestBody(UploadFileBean fileBean, FileUploadObserver<UploadFileBean> fileUploadObserver) {
        this.uploadFileBean = fileBean;
        this.mRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileBean.getFilePath()));
        this.fileUploadObserver = fileUploadObserver;
    }


    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            // Log Interceptor
            mRequestBody.writeTo(sink);
            return;
        }
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        mRequestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }


        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            LogUtil.d("上传中---" + String.valueOf(bytesWritten) + "总长度---" + contentLength());
            if (fileUploadObserver != null) {
                fileUploadObserver.onProgressChange(uploadFileBean, bytesWritten, contentLength());
            }

        }

    }
}
