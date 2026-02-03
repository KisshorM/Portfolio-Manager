from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

def summarize_text(text):
    model_name = "Falconsai/text_summarization"
    
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    model = AutoModelForSeq2SeqLM.from_pretrained(model_name)
    
    inputs = tokenizer.encode(
        "summarize: " + text, 
        return_tensors="pt", 
        max_length=512, 
        truncation=True
    )
    
    outputs = model.generate(
        inputs, 
        max_length=150, 
        min_length=30, 
        length_penalty=2.0, 
        num_beams=4, 
        early_stopping=True
    )
    
    return tokenizer.decode(outputs[0], skip_special_tokens=True)

article = """
The Apollo program, also known as Project Apollo, was the third United States human spaceflight 
program carried out by the National Aeronautics and Space Administration (NASA), which 
succeeded in preparing and landing the first humans on the Moon from 1968 to 1972.
"""

print(summarize_text(article))