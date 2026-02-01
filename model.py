
from transformers import BertForSequenceClassification, BertTokenizer
import torch

quantized_model_path = "AventIQ-AI/sentiment-analysis-for-stock-market-sentiment"
quantized_model = BertForSequenceClassification.from_pretrained(quantized_model_path)
quantized_model.eval()
quantized_model.half()

# Load tokenizer
tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")

def predict_sentiment(text):
    # Define a test sentence
    #test_sentence = "Apple Inc. reported stronger-than-expected earnings this quarter, driven by robust iPhone sales and growth in its services segment. Investors reacted positively, pushing the stock up by 3% in after-hours trading. Analysts believe Apple is well-positioned for continued growth, especially with the upcoming product launches.On the other hand, Tesla shares dropped by 5% after the company missed its delivery targets and announced a temporary halt at its Berlin factory due to supply chain issues. Market sentiment remains cautious around Tesla, with concerns about rising competition in the EV space and fluctuating production numbers."

    test_sentence = text
    # Tokenize input
    inputs = tokenizer(test_sentence, return_tensors="pt", padding=True, truncation=True, max_length=128)

    # Ensure input tensors are in correct dtype
    inputs["input_ids"] = inputs["input_ids"].long()  # Convert to long type
    inputs["attention_mask"] = inputs["attention_mask"].long()  # Convert to long type

    # Make prediction
    with torch.no_grad():
        outputs = quantized_model(**inputs)

    # Get predicted class
    predicted_class = torch.argmax(outputs.logits, dim=1).item()
    # print(f"Predicted Class: {predicted_class}")


    label_mapping = {0: "very_negative", 1: "negative", 2: "neutral", 3: "positive", 4: "very_positive"}  # Example

    predicted_label = label_mapping[predicted_class]
    return predicted_label


