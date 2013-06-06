/*
 * Copyright 2011-13 Fraunhofer ISE
 *
 * This file is part of OpenMUC.
 * For more information visit http://www.openmuc.org
 *
 * OpenMUC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * OpenMUC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenMUC.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package helper;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.restlet.data.Parameter;
import org.restlet.ext.ssl.SslContextFactory;
import org.restlet.util.Series;

/**
 * Variant of the SslContextFactory that disables all verification of the remote certificate. This is only here for
 * demonstration purposes. Using this class in production is a BAD idea.
 * 
 * @author Karsten Mueller-Bier
 */
public class InsecureSslContextFactory extends SslContextFactory {

	TrustManager tm = new InsecureTrustManager();

	@Override
	public SSLContext createSslContext() throws Exception {
		final SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { tm }, null);
		return context;
	}

	@Override
	public void init(Series<Parameter> paramSeries) {
	}

}

/**
 * Insecure variant of a TrustManager. Don't use this class in productive code! If you do, your client will accept all
 * certificates from the remote station, even invalid ones.
 * 
 * @author Karsten Mueller-Bier
 */
class InsecureTrustManager implements X509TrustManager {

	private final X509Certificate acceptedCertificates[] = new X509Certificate[0];

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return acceptedCertificates;
	}

}
