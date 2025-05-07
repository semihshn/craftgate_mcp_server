package com.craftgate.mcp.mcp_craftgate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.craftgate.Craftgate;
import io.craftgate.model.Currency;
import io.craftgate.model.PaymentProvider;
import io.craftgate.model.PaymentSource;
import io.craftgate.model.PaymentStatus;
import io.craftgate.model.PaymentType;
import io.craftgate.request.SearchPaymentsRequest;
import io.craftgate.response.ReportingPaymentListResponse;

/**
 * MCP server için Craftgate ödeme bilgisi sorgulama tool'u.
 */
@Service
public class CraftgatePaymentInfoTool {

    private final Craftgate craftgate;

    public CraftgatePaymentInfoTool(
            @Value("${craftgate.api-key}") String apiKey,
            @Value("${craftgate.secret-key}") String secretKey,
            @Value("${craftgate.base-url:https://sandbox-api.craftgate.io}") String baseUrl
    ) {
        this.craftgate = new Craftgate(apiKey, secretKey, baseUrl);
    }

    /**
     * MCP tool: Tüm SearchPaymentsRequest parametreleriyle ödeme araması yapar. Parametreler opsiyoneldir, gelenler iletilir.
     * @param page Sayfa numarası
     * @param size Sayfa boyutu
     * @param paymentId Ödeme ID
     * @param paymentTransactionId Ödeme işlem ID
     * @param buyerMemberId Alıcı üye ID
     * @param subMerchantMemberId Alt üye işyeri ID
     * @param conversationId Conversation ID
     * @param externalId External ID
     * @param orderId Order ID
     * @param paymentType Ödeme tipi
     * @param paymentProvider Ödeme sağlayıcı
     * @param paymentStatus Ödeme durumu
     * @param paymentSource Ödeme kaynağı
     * @param paymentChannel Ödeme kanalı
     * @param binNumber Bin numarası
     * @param lastFourDigits Son dört hane
     * @param currency Para birimi
     * @param minPaidPrice Min ödenen tutar
     * @param maxPaidPrice Max ödenen tutar
     * @param installment Taksit
     * @param isThreeDS 3DS mi?
     * @param minCreatedDate Min oluşturulma tarihi (yyyy-MM-dd'T'HH:mm:ss)
     * @param maxCreatedDate Max oluşturulma tarihi (yyyy-MM-dd'T'HH:mm:ss)
     * @return ReportingPaymentListResponse
     */
    @Tool(
        description = """
        Craftgate üzerindeki ödemeleri detaylı şekilde aramak ve listelemek için kullanılır.
        Tüm parametreler opsiyoneldir; sadece ihtiyacınız olan filtreleri göndererek arama yapabilirsiniz.
        Bu tool, ödeme yönetimi, raporlama, müşteri destek süreçleri veya finansal analiz gibi senaryolarda kullanılabilir.

        Parametreler:
        - page: Sonuçların kaçıncı sayfası gösterilsin? (örn: 0)
        - size: Sayfa başına kaç sonuç gösterilsin? (örn: 20)
        - paymentId: Belirli bir ödeme ID'siyle arama yapar.
        - paymentTransactionId: Belirli bir ödeme işlem ID'siyle arama yapar.
        - buyerMemberId: Alıcı üye ID'siyle filtreler.
        - subMerchantMemberId: Alt üye işyeri ID'siyle filtreler.
        - conversationId: Dış sistemlerle entegre edilen işlemler için benzersiz kimlik.
        - externalId: Dış sistemdeki ödeme ID'siyle arama yapar.
        - orderId: Sipariş ID'siyle filtreler.
        - paymentType: Ödeme tipi (örn: CARD_PAYMENT, DEPOSIT_PAYMENT, APM).
        - paymentProvider: Ödeme sağlayıcı (örn: GARANTI, YKB, PAPARA).
        - paymentStatus: Ödeme durumu (örn: SUCCESS, FAILURE, REFUNDED).
        - paymentSource: Ödeme kaynağı (örn: API, CHECKOUT_FORM).
        - paymentChannel: Ödeme kanalı (örn: WEB, MOBILE).
        - binNumber: Kartın ilk 6 hanesiyle filtreler.
        - lastFourDigits: Kartın son 4 hanesiyle filtreler.
        - currency: Para birimi (örn: TRY, USD, EUR).
        - minPaidPrice: Minimum ödenen tutar.
        - maxPaidPrice: Maksimum ödenen tutar.
        - installment: Taksit sayısı.
        - isThreeDS: 3D Secure ile mi ödendi? (true/false)
        - minCreatedDate: Başlangıç tarihi (örn: 2024-01-01T00:00:00)
        - maxCreatedDate: Bitiş tarihi (örn: 2024-01-31T23:59:59)

        Parametrelerin hiçbiri zorunlu değildir. Sadece ihtiyacınız olanları gönderin; örneğin sadece bir tarih aralığı ve ödeme durumu ile arama yapabilirsiniz.

        Dönüş: 
        - Sonuç olarak, arama kriterlerine uyan ödemelerin listesini ve toplam sayısını içeren bir ReportingPaymentListResponse nesnesi döner.

        Örnek Kullanım:
        - Son 1 ayda başarılı olan ödemeleri listelemek için:
          page=0, size=20, paymentStatus=SUCCESS, minCreatedDate="2024-05-01T00:00:00", maxCreatedDate="2024-05-31T23:59:59"
        """
    )
    public ReportingPaymentListResponse getAllPayments(
            @ToolParam(description = "Sayfa numarası") Integer page,
            @ToolParam(description = "Sayfa boyutu") Integer size,
            @ToolParam(description = "Ödeme ID") Long paymentId,
            @ToolParam(description = "Ödeme işlem ID") Long paymentTransactionId,
            @ToolParam(description = "Alıcı üye ID") Long buyerMemberId,
            @ToolParam(description = "Alt üye işyeri ID") Long subMerchantMemberId,
            @ToolParam(description = "Conversation ID") String conversationId,
            @ToolParam(description = "External ID") String externalId,
            @ToolParam(description = "Order ID") String orderId,
            @ToolParam(description = "Ödeme tipi") PaymentType paymentType,
            @ToolParam(description = "Ödeme sağlayıcı") PaymentProvider paymentProvider,
            @ToolParam(description = "Ödeme durumu") PaymentStatus paymentStatus,
            @ToolParam(description = "Ödeme kaynağı") PaymentSource paymentSource,
            @ToolParam(description = "Ödeme kanalı") String paymentChannel,
            @ToolParam(description = "Bin numarası") String binNumber,
            @ToolParam(description = "Son dört hane") String lastFourDigits,
            @ToolParam(description = "Para birimi") Currency currency,
            @ToolParam(description = "Min ödenen tutar") BigDecimal minPaidPrice,
            @ToolParam(description = "Max ödenen tutar") BigDecimal maxPaidPrice,
            @ToolParam(description = "Taksit") Integer installment,
            @ToolParam(description = "3DS mi?") Boolean isThreeDS,
            @ToolParam(description = "Min oluşturulma tarihi (yyyy-MM-dd'T'HH:mm:ss)") String minCreatedDate,
            @ToolParam(description = "Max oluşturulma tarihi (yyyy-MM-dd'T'HH:mm:ss)") String maxCreatedDate
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        SearchPaymentsRequest.SearchPaymentsRequestBuilder builder = SearchPaymentsRequest.builder();

        if (page != null) builder.page(page);
        if (size != null) builder.size(size);
        if (paymentId != null) builder.paymentId(paymentId);
        if (paymentTransactionId != null) builder.paymentTransactionId(paymentTransactionId);
        if (buyerMemberId != null) builder.buyerMemberId(buyerMemberId);
        if (subMerchantMemberId != null) builder.subMerchantMemberId(subMerchantMemberId);
        if (conversationId != null) builder.conversationId(conversationId);
        if (externalId != null) builder.externalId(externalId);
        if (orderId != null) builder.orderId(orderId);
        if (paymentType != null) builder.paymentType(paymentType);
        if (paymentProvider != null) builder.paymentProvider(paymentProvider);
        if (paymentStatus != null) builder.paymentStatus(paymentStatus);
        if (paymentSource != null) builder.paymentSource(paymentSource);
        if (paymentChannel != null) builder.paymentChannel(paymentChannel);
        if (binNumber != null) builder.binNumber(binNumber);
        if (lastFourDigits != null) builder.lastFourDigits(lastFourDigits);
        if (currency != null) builder.currency(currency);
        if (minPaidPrice != null) builder.minPaidPrice(minPaidPrice);
        if (maxPaidPrice != null) builder.maxPaidPrice(maxPaidPrice);
        if (installment != null) builder.installment(installment);
        if (isThreeDS != null) builder.isThreeDS(isThreeDS);
        if (minCreatedDate != null && !minCreatedDate.isEmpty())
            builder.minCreatedDate(LocalDateTime.parse(minCreatedDate, formatter));
        if (maxCreatedDate != null && !maxCreatedDate.isEmpty())
            builder.maxCreatedDate(LocalDateTime.parse(maxCreatedDate, formatter));

        SearchPaymentsRequest request = builder.build();
        return craftgate.paymentReporting().searchPayments(request);
    }
}