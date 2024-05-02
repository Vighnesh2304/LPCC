MNT = {}
MDT = {}
MDT_COUNTER = 0
def first_pass(input_file):
    global MDT_COUNTER
    inside_macro = False
    macro_name = ""
    with open(input_file, "r") as file:
        for line in file:
            words = line.strip().split()
            if "MACRO" in words:
                inside_macro = True
                macro_name = words[0]
                MNT[macro_name] = MDT_COUNTER
                MDT[MDT_COUNTER] = []
            elif "MEND" in words:
                inside_macro = False
                MDT_COUNTER += 1
            elif inside_macro:
                MDT[MDT_COUNTER].append(line.strip())
def second_pass(input_file, output_file):
    with open(input_file, "r") as infile, open(output_file, "w") as outfile:
        inside_macro = False
        for line in infile:
            words = line.strip().split()
            if words[0] in MNT:
                macro_start = MNT[words[0]]
                for macro_line in MDT[macro_start]:
                    expanded_line = macro_line
                    for i in range(1, len(words)):
                        expanded_line = expanded_line.replace(f"&{i}", words[i])
                    outfile.write(expanded_line + "\n")
            else:
                outfile.write(line)

input_file = "macroIC.txt"
intermediate_code_file = "intermediate_code.txt"
first_pass(input_file)
second_pass(input_file, intermediate_code_file)